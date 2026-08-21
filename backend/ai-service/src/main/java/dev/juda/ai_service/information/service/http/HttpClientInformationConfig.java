package dev.juda.ai_service.information.service.http;

import dev.juda.ai_service.information.service.exception.TemplateNotExistsException;
import dev.juda.ai_service.information.service.exception.UserNonExistsException;
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

    @Bean("templates")
    RestClient templatesRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://templates-service/templates")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    LOG.warn("Template not exists");
                    throw new TemplateNotExistsException();
                })
                .build();
    }

    @Bean("users")
    RestClient usersRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://users-service/users")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    LOG.warn("User not exists");
                    throw new UserNonExistsException();
                })
                .build();
    }
}
