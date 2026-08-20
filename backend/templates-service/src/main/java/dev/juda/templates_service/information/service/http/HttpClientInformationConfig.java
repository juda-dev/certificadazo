package dev.juda.templates_service.information.service.http;

import dev.juda.templates_service.information.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientInformationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientInformationConfig.class);

    @Bean
    RestClient usersRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://users-service/users/")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    LOG.warn("User not found");
                    throw new UserNotFoundException();
                })
                .build();
    }
}
