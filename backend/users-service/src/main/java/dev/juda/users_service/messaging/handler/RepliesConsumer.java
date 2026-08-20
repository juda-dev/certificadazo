package dev.juda.users_service.messaging.handler;

import dev.juda.users_service.messaging.app.ReplyInbox;
import dev.juda.users_service.messaging.dto.in.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class RepliesConsumer {
    private final ReplyInbox replyInbox;
    private static final Logger LOG =  LoggerFactory.getLogger(RepliesConsumer.class);

    public RepliesConsumer(ReplyInbox replyInbox) {
        this.replyInbox = replyInbox;
    }

    @Bean
    public Consumer<Message<Reply<?>>> handleReplies() {
        LOG.trace("Initiating consumption of responses from the authentication service");
        return message -> {
            LOG.debug("Response received {}", message.getPayload());

            String correlationId = message.getHeaders()
                    .get("correlationId", String.class);

            replyInbox.complete(correlationId,  message.getPayload());

            LOG.info("Completed complete with correlationId {} in ReplyInbox", correlationId);
        };
    }
}
