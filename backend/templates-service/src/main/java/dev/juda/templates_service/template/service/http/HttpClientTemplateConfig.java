package dev.juda.templates_service.template.service.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import dev.juda.templates_service.template.presentation.exception.DepartmentNotFoundException;

@Configuration
public class HttpClientTemplateConfig {

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
    RestClient departmentsRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://departments-service/departments/")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new DepartmentNotFoundException();
                })
                .build();
    }
}
