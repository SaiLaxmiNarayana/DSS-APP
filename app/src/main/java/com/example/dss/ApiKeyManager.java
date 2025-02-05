package com.example.dss;

public class ApiKeyManager {


    private static final String API_KEY = "my7P70GUOLiOjhS4HJQ0wPGb4Rni0Otu";

    // Singleton pattern
    private static ApiKeyManager instance;

    private ApiKeyManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized ApiKeyManager getInstance() {
        if (instance == null) {
            instance = new ApiKeyManager();
        }
        return instance;
    }

    public String getApiKey() {
        return API_KEY;
    }
}
