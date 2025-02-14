package com.test.channelplay.utils;

public class AuthManager_API {
    private static String authToken;

    public static void setAuthToken(String token) {
        authToken = token;
    }
    
    public static String getAuthToken() {
        if (authToken == null || authToken.isEmpty()) {
            throw new RuntimeException("Auth token is not set. Ensure login UI test runs first.");
        }
        return authToken;
    }

}
