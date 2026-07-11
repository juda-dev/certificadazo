package dev.juda.ai_service.template.service.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientTemplateConfig {

    @Bean("departments")
    RestClient departmentsRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://departments-service/departments")
                .build();
    }
}
