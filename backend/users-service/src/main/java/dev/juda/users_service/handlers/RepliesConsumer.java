package dev.juda.users_service.handlers;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import dev.juda.users_service.messaging.ReplyInbox;
import dev.juda.users_service.models.dto.messaging.Reply;

@Configuration
public class RepliesConsumer {
    private final ReplyInbox replyInbox;

    public RepliesConsumer(ReplyInbox replyInbox) {
        this.replyInbox = replyInbox;
    }

    @Bean
    public Consumer<Message<Reply<?>>> handleReplies() {
        return message -> {
            String correlationId = message.getHeaders()
                    .get("correlationId", String.class);

            replyInbox.complete(correlationId,  message.getPayload());
        };
    }
}
