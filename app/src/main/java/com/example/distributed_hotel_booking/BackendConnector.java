package com.example.distributed_hotel_booking;

// singleton class to connect to the backend
public class BackendConnector {
    private static BackendConnector instance = null;

    private BackendConnector() {
    }

    public static BackendConnector getInstance() {
        if (instance == null) {
            instance = new BackendConnector();
        }
        return instance;
    }

    public void connect() {
        // connect to the backend
    }

    public void disconnect() {
        // disconnect from the backend
    }

    public void sendRequest(String request) {
        // send request to the backend
    }

    public String getResponse() {
        // get response from the backend
        return "response";
    }
}
