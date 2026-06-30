package dev.juda.auth_service.service.implementation;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import dev.juda.auth_service.messaging.dto.CreateUserRequest;
import dev.juda.auth_service.models.dto.request.AuthRequest;
import dev.juda.auth_service.models.dto.response.AuthResponse;
import dev.juda.auth_service.models.dto.response.CreateUserReply;
import dev.juda.auth_service.service.exception.InvalidCredentialsException;
import dev.juda.auth_service.service.exception.RoleNotFoundException;
import dev.juda.auth_service.service.exception.UserNotCreatedException;
import dev.juda.auth_service.service.interfaces.AuthService;
import dev.juda.auth_service.util.enums.Roles;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@Service
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final Keycloak keycloak;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public AuthServiceImpl(Keycloak keycloak, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.keycloak = keycloak;
    }

    @Override
    public CreateUserReply create(CreateUserRequest req) {
        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(req.documentId());
            user.setEmail(req.email());
            user.setFirstName(req.firstName());
            user.setLastName(req.lastName());
            user.setEmailVerified(true);
            user.setEnabled(true);

            UsersResource usersResource = keycloak.realm(realm).users();
            try (Response response = usersResource.create(user)) {
                if (response.getStatus() != 201) {
                    throw new UserNotCreatedException();
                }

                String location = response.getHeaderString("Location");
                String userId = location.substring(location.lastIndexOf('/') + 1);

                assignRole(userId);

                setPassword(userId, req.password());

                return new CreateUserReply(UUID.fromString(userId));
            }
        } catch (Exception e) {
            throw new UserNotCreatedException();
        }
    }

    private void assignRole(String userId) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            RoleRepresentation role = keycloak.realm(realm).roles()
                    .get(Roles.USER.getName()).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(role));
        } catch (NotFoundException e) {
            throw new RoleNotFoundException();
        }
    }

    private void setPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(realm).users().get(userId)
                .resetPassword(credential);
    }

    @Override
    public AuthResponse login(AuthRequest req) {
        try {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("username", req.username());
            body.add("password", req.password());
            body.add("grant_type", "password");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> tokenData = response.getBody();
                AuthResponse authResponse = new AuthResponse(
                        (String) tokenData.get("access_token"),
                        (String) tokenData.get("refresh_token"),
                        ((Number) tokenData.get("expires_in")).intValue(),
                        (String) tokenData.get("token_type"));
                return authResponse;
            }

            throw new RuntimeException("Empty authentication response");

        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            throw new InvalidCredentialsException();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected communication error with the authentication server", e);
        }
    }

}
