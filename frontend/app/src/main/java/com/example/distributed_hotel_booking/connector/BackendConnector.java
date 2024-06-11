package com.example.distributed_hotel_booking.connector;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


// singleton class to connect to the backend
public class BackendConnector {
    private static BackendConnector instance = null;
    private Socket socket;

    private static Gson gson = new Gson();

    private boolean isConnected;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private BackendConnector() {
        this.isConnected = false;
        this.socket = null;
        this.out = null;
        this.in = null;
    }

    // singleton pattern
    public static BackendConnector getInstance() {
        if (instance == null) {
            instance = new BackendConnector();
        }
        return instance;
    }

    public void connectToServer() {
        // connect to the backend
        try {
            this.socket = new Socket("192.168.31.61", 5001);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.isConnected = true;
            Log.d("Connection from phone", "Successfully connected to the server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TransmissionObject sendRequest(TransmissionObject request) {
        if (!this.isConnected) {
            this.connectToServer();
            Log.d("RECONNECT", "Reconnected to the server");
        }

        // send request to the backend
        try {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
            String json = gson.toJson(request);

            this.out.writeObject(json);
            this.out.flush();
            Log.d("Connection from phone", "Request sent to the server");
            String response = (String) this.in.readObject();
            Log.d("Connection from phone", "Response received from the server");
            Log.d("Response", response);

            return decodeResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TransmissionObject();
    }

    public static TransmissionObject decodeResponse(String response) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.fromJson(response, TransmissionObject.class);
    }

    public static TransmissionObject createTransmissionObject(TransmissionObjectType type) {
        TransmissionObject transmissionObject = new TransmissionObject();
        transmissionObject.type = type;
        return transmissionObject;
    }
}
