package dev.juda.ai_service.shared.messaging.handler;

import dev.juda.ai_service.shared.messaging.app.ReplyInbox;
import dev.juda.ai_service.shared.messaging.dto.in.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class RepliesConsumer {
    private final ReplyInbox replyInbox;
    private static final Logger LOG = LoggerFactory.getLogger(RepliesConsumer.class);

    public RepliesConsumer(ReplyInbox replyInbox) {
        this.replyInbox = replyInbox;
    }

    @Bean
    Consumer<Message<Reply<?>>> handleReplies() {
        return message -> {
            String correlationId = message.getHeaders()
                    .get("correlationId", String.class);

            LOG.info("Response received with correlationId {}", correlationId);
            replyInbox.complete(correlationId, message.getPayload());
        };
    }
}
