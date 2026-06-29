package dev.juda.users_service.models.enums;

public enum ReplyStatus {
    SUCCESS,
    ERROR;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
