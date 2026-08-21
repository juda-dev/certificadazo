package dev.juda.ai_service.information.configuration;

import dev.juda.ai_service.template.service.exception.PromptNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class DeepSeekAiConfig {

    private static final Logger LOG =  LoggerFactory.getLogger(DeepSeekAiConfig.class);

    @Bean("deepSeekChatClient")
    ChatClient deepSeekChatClient(DeepSeekChatModel deepSeekChatModel) {
        String defaultSystem;
        try {
            defaultSystem = new ClassPathResource("prompts/deepseek-default-system.txt")
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Error trying to get the default system");
            throw new PromptNotFoundException();
        }

        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem(defaultSystem)
                .build();
    }
}
