package com.example.distributed_hotel_booking.connector;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.example.distributed_hotel_booking.data.Room;
import com.google.gson.Gson;

// singleton class to connect to the backend
public class BackendConnector {
    private static BackendConnector instance = null;
    private Socket socket;

    private static final Gson gson = new Gson();

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
            // PUT THE IP OF THE DEVELOPMENT DEVICE HERE
            this.socket = new Socket("192.168.1.28", 5000);
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
            String json = gson.toJson(request);
            System.out.println("Json request " + json);

            this.out.writeObject(json);
            this.out.flush();
            Log.d("Connection from phone", "Request sent to the server");
            String response = (String) this.in.readObject();
            Log.d("Connection from phone", "Response received from the server");

            return decodeResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TransmissionObject();
    }

    public static TransmissionObject decodeResponse(String response) {
        return gson.fromJson(response, TransmissionObject.class);
    }

    public static TransmissionObject createTransmissionObject(TransmissionObjectType type) {
        TransmissionObject transmissionObject = new TransmissionObject();
        transmissionObject.type = type;
        return transmissionObject;
    }

    public void disconnect() {
        // disconnect from the backend
    }

//    public List<Room> getAllRooms() {
//        TransmissionObject request = createTransmissionObject(TransmissionObjectType.GET_ALL_ROOMS);
//        TransmissionObject response = sendRequest(request);
//        // Assuming the response contains a List<Room>
//        return response.rooms; // Obviously not final !
//    }
}
