package dev.juda.ai_service.template.service.exception;

public class TimeoutCommandException extends RuntimeException {
    public TimeoutCommandException(String message) {
        super(message);
    }
}
