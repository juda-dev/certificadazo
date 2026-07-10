package dev.juda.ai_service.information.service.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientInformationConfig {

    @Bean("templates")
    RestClient templatesRestClient(@LoadBalanced RestClient.Builder builder) {
        return builder
                .baseUrl("http://templates-service/templates/")
                .build();
    }
}
