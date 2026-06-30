package dev.juda.users_service.service.exception;

public class ExistingUserException extends RuntimeException{
    
    private String field;
    
    public ExistingUserException(String field){
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
