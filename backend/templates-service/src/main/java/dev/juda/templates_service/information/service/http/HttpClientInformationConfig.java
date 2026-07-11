package dev.juda.templates_service.information.service.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import dev.juda.templates_service.information.service.exception.UserNotFoundException;

@Configuration
public class HttpClientInformationConfig {

    @Bean
    RestClient usersRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://users-service/users/")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new UserNotFoundException();
                })
                .build();
    }
}
