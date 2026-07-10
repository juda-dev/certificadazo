package dev.juda.ai_service.shared.messaging.handler;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import dev.juda.ai_service.shared.messaging.app.ReplyInbox;
import dev.juda.ai_service.shared.messaging.dto.in.Reply;

@Configuration
public class RepliesConsumer {
    private final ReplyInbox replyInbox;

    public RepliesConsumer(ReplyInbox replyInbox) {
        this.replyInbox = replyInbox;
    }

    @Bean
    Consumer<Message<Reply<?>>> handleReplies() {
        return message -> {
            String correlationId = message.getHeaders()
                    .get("correlationId", String.class);

            replyInbox.complete(correlationId, message.getPayload());
        };
    }
}
