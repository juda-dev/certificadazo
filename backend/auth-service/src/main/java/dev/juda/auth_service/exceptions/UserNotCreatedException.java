package dev.juda.auth_service.exceptions;

public class UserNotCreatedException extends RuntimeException{
    public UserNotCreatedException(String message){
        super(message);
    }
}
