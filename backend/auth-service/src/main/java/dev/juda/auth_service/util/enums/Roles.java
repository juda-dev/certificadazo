package dev.juda.auth_service.util.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String name;

    private Roles(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
