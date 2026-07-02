package dev.juda.auth_service.service.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {

    @Bean
    RestClient apiRestClient(RestClient.Builder builder) {
        return builder.build();
    }
}
