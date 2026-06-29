package dev.juda.users_service.exceptions;

public class TimeoutCommandException extends RuntimeException{
    public TimeoutCommandException(String message){
        super(message);
    }
}
