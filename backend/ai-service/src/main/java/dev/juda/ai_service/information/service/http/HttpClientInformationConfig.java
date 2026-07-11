package dev.juda.ai_service.information.service.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import dev.juda.ai_service.information.service.exception.TemplateNotExistsException;
import dev.juda.ai_service.information.service.exception.UserNonExistsException;

@Configuration
public class HttpClientInformationConfig {

    @Bean("templates")
    RestClient templatesRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://templates-service/templates")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new TemplateNotExistsException();
                })
                .build();
    }

    @Bean("users")
    RestClient usersRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://users-service/users")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new UserNonExistsException();
                })
                .build();
    }
}
