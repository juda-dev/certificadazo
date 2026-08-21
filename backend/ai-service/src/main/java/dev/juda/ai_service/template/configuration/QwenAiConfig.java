package dev.juda.ai_service.template.configuration;

import dev.juda.ai_service.template.service.exception.PromptNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class QwenAiConfig {

    private static final Logger LOG = LoggerFactory.getLogger(QwenAiConfig.class);

    @Bean("qwenChatModel")
    OpenAiChatModel qwenChatModel(
            @Value("${qwen.base-url}") String baseUrl,
            @Value("${qwen.api-key}") String apiKey,
            @Value("${qwen.model}") String model,
            @Value("${qwen.temperature}") double temperature,
            @Value("${qwen.max-tokens}") int maxTokens) {

        OpenAiChatOptions defaultOptions = OpenAiChatOptions.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(Duration.ofMinutes(5))
                .build();

        return OpenAiChatModel.builder()
                .options(defaultOptions)
                .build();
    }

    @Bean("qwenChatClient")
    ChatClient qwenChatClient(OpenAiChatModel qwenChatModel) {
        String defaultSystem;
        try {
            defaultSystem = new ClassPathResource("prompts/qwen-default-system.txt")
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Error trying to get the default system");
            throw new PromptNotFoundException();
        }

        return ChatClient.builder(qwenChatModel)
                .defaultSystem(defaultSystem)
                .build();
    }
}
