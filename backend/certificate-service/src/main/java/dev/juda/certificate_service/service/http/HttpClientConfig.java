package dev.juda.certificate_service.service.http;

import dev.juda.certificate_service.service.exception.TemplateNotFoundException;
import dev.juda.certificate_service.service.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientConfig.class);

    @Bean
    @Primary
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    @LoadBalanced
    RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean("users")
    RestClient usersRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://users-service/users")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    LOG.warn("User not found");
                    throw new UserNotFoundException();
                })
                .build();
    }

    @Bean("templates")
    RestClient templatesRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://templates-service/templates")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    LOG.warn("Template not found");
                    throw new TemplateNotFoundException();
                })
                .build();
    }
}
