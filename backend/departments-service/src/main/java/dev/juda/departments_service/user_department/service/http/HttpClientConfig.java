package dev.juda.departments_service.user_department.service.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import dev.juda.departments_service.user_department.presentation.exception.NonExistsUser;

@Configuration
public class HttpClientConfig {

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

    @Bean
    RestClient usersRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://users-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new NonExistsUser();
                })
                .build();
    }
}
