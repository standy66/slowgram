package com.letsplaydota.slowgram.models;

/**
 * Created by andrew on 22.05.15.
 */
public class ServerConnector {
    private static MessagingServiceProvider instance = null;
    private static String token;


    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ServerConnector.token = token;
    }

    public static MessagingServiceProvider get() {
        if (instance == null) {
            instance = new RemoteMessagingServiceProvider("188.166.109.92", 3000);
        }
        return instance;
    }
}
