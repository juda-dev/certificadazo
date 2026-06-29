package dev.juda.users_service.messaging.app;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import dev.juda.users_service.messaging.dto.in.Reply;

@Component
public class ReplyInbox {
    private final ConcurrentHashMap<String, CompletableFuture<Reply<?>>> pending = new ConcurrentHashMap<>();

    public CompletableFuture<Reply<?>> register(String correlationId){
        CompletableFuture<Reply<?>> future = new CompletableFuture<>();
        pending.put(correlationId, future);

        return future;
    }

    public void complete(String correlationId, Reply<?> reply){
        if (correlationId == null) {
            throw new NullPointerException("correlationId can't be null");
        }
        CompletableFuture<Reply<?>> future = pending.remove(correlationId);

        if (future != null) {
            future.complete(reply);
        }
    }
}
