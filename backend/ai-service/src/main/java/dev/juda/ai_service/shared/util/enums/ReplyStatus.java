package dev.juda.ai_service.shared.util.enums;

public enum ReplyStatus {
    SUCCESS,
    ERROR;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
