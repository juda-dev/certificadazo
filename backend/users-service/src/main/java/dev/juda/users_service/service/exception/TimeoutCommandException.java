package dev.juda.users_service.service.exception;

public class TimeoutCommandException extends RuntimeException{
    public TimeoutCommandException(String message){
        super(message);
    }
}
