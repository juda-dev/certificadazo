package dev.juda.users_service.exceptions;

public class TimeoutException extends RuntimeException{
    public TimeoutException(String message){
        super(message);
    }
}
