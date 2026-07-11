package dev.juda.ai_service.information.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import dev.juda.ai_service.template.service.exception.PromptNotFoundException;

@Configuration
public class DeepSeekAiConfig {

    @Bean("deepSeekChatClient")
    ChatClient deepSeekChatClient(DeepSeekChatModel deepSeekChatModel) {
        String defaultSystem;
        try {
            defaultSystem = new ClassPathResource("prompts/deepseek-default-system.txt")
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PromptNotFoundException();
        }

        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem(defaultSystem)
                .build();
    }
}
