package dev.juda.ai_service.presentation.exception;

public class TimeoutCommandException extends RuntimeException {
    public TimeoutCommandException(String message) {
        super(message);
    }
}
