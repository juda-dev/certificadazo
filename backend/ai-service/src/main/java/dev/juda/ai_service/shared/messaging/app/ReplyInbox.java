package dev.juda.ai_service.shared.messaging.app;

import dev.juda.ai_service.shared.messaging.dto.in.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReplyInbox {

    private final ConcurrentHashMap<String, CompletableFuture<Reply<?>>> pending = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(ReplyInbox.class);

    public CompletableFuture<Reply<?>> register(String correlationId) {
        LOG.trace("Starting registration with correlationId {} in ReplyInbox", correlationId);
        CompletableFuture<Reply<?>> future = new CompletableFuture<>();
        pending.put(correlationId, future);

        LOG.info("Registered CompletableFuture {}", correlationId);
        return future;
    }

    public void complete(String correlationId, Reply<?> reply) {
        LOG.trace("Starting complete with correlationId {} in ReplyInbox", correlationId);
        if (correlationId == null) {
            LOG.error("CorrelationId is null");
            throw new NullPointerException("correlationId can't be null");
        }
        CompletableFuture<Reply<?>> future = pending.remove(correlationId);

        if (future != null) {
            future.complete(reply);
        }
        LOG.info("Completed complete with correlationId {} in ReplyInbox", correlationId);
    }
}
