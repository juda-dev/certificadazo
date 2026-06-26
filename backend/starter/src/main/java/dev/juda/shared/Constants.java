package dev.juda.shared;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    public static final String USER_EVENT_TOPIC = "user-event";
    public static final String AUTH_EVENT_TOPIC = "auth-event";
    public static final String TEMPLATE_EVENT_TOPIC = "template-event";
    public static final String IA_EVENT_TOPIC = "ia-event";
    public static final String NOTIFICATION_EVENT_TOPIC = "notification-event";

    public static final String KEYCLOAK_SERVER_URL = "http://localhost:8080";
    public static final String KEYCLOAK_REALM = "certificadazo";
    public static final String KEYCLOAK_CLIENT_ID = "certificadazo-client";
}
