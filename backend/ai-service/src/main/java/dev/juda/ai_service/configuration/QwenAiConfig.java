package dev.juda.ai_service.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import dev.juda.ai_service.presentation.exception.PromptNotFoundException;

@Configuration
public class QwenAiConfig {

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
            throw new PromptNotFoundException();
        }

        return ChatClient.builder(qwenChatModel)
                .defaultSystem(defaultSystem)
                .build();
    }
}
