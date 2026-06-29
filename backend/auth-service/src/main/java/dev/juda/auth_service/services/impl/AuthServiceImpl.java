package dev.juda.auth_service.services.impl;

import java.util.Collections;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.juda.auth_service.exceptions.RoleNotFoundException;
import dev.juda.auth_service.exceptions.UserNotCreatedException;
import dev.juda.auth_service.models.dto.messaging.CreateUserRequest;
import dev.juda.auth_service.models.dto.response.CreateUserReply;
import dev.juda.auth_service.models.enums.Roles;
import dev.juda.auth_service.services.AuthService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@Service
public class AuthServiceImpl implements AuthService {

    private final Keycloak keycloak;
    private final String realm;

    public AuthServiceImpl(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public CreateUserReply create(CreateUserRequest req) {
        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(req.documentId());
            user.setEmailVerified(true);
            user.setEnabled(true);

            UsersResource usersResource = keycloak.realm(realm).users();
            try (Response response = usersResource.create(user)) {
                if (response.getStatus() != 201) {
                    String errorBody = response.readEntity(String.class);
                    throw new UserNotCreatedException(
                            "Failed to create user in Keycloak: " + errorBody);
                }

                String location = response.getHeaderString("Location");
                String userId = location.substring(location.lastIndexOf('/') + 1);

                assignRole(userId);

                setPassword(userId, req.password());

                return new CreateUserReply(UUID.fromString(userId));
            }
        } catch (Exception e) {
            throw new UserNotCreatedException(
                    "Failed to create user in Keycloak: " + e.getMessage());
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

}
