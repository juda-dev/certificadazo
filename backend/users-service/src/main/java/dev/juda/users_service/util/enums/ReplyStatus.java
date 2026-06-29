package dev.juda.users_service.util.enums;

public enum ReplyStatus {
    SUCCESS,
    ERROR;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
