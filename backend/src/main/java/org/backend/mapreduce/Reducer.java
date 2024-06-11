package org.backend.mapreduce;

import org.backend.misc.DateRange;
import org.backend.misc.Pair;
import org.backend.misc.SearchFilter;
import org.backend.modules.Booking;
import org.backend.modules.Manager;
import org.backend.modules.Review;
import org.backend.modules.Room;

import java.awt.print.Book;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Reducer extends Thread {
    private int workerConnectionPort, masterConnectionPort;
    private String masterHost;
    private static int NUM_WORKERS;
    private static int workersFinished = 0;
    private static boolean allWorkersFinished = false;
    protected final List<Pair<Integer, Object>> intermediateResultsList = new ArrayList<>();

    public void run() {
        // Initialize the reducer configuration
        initReducerConfig();

        // Start the worker connection handler
        WorkerConnectionHandler workerConnectionHandler = new WorkerConnectionHandler(workerConnectionPort);
        workerConnectionHandler.start();

        while (true) {
            // Wait for all workers to finish
            synchronized (this) {
                while (!allWorkersFinished) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // Reduce the intermediate results
            Pair<Integer, Object> finalResults = reduce(intermediateResultsList);
            System.err.println(STR."Final Results -> \{finalResults}\n");

            // send results to master
            sendToMaster(finalResults, workerConnectionHandler.action);

            // Clear the intermediate results list
            synchronized (intermediateResultsList) {
                intermediateResultsList.clear();
            }

            // Reset the workersFinished and allWorkersFinished flags
            synchronized (this) {
                allWorkersFinished = false;
                workersFinished = 0;
            }
        }
    }

    private void initReducerConfig() {
        // Read configuration from properties file
        try (FileReader reader = new FileReader(STR."\{System.getProperty("user.dir")}\\src\\main\\java\\org\\backend\\mapreduce\\data\\reducerConfig.properties")) {
            Properties properties = new Properties();
            properties.load(reader);

            masterHost = (String) properties.getProperty("masterHost");
            workerConnectionPort = Integer.parseInt(properties.getProperty("workerConnectionPort"));
            masterConnectionPort = Integer.parseInt(properties.getProperty("masterConnectionPort"));
            NUM_WORKERS = Integer.parseInt(properties.getProperty("numWorkers"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class WorkerConnectionHandler extends Thread {
        private final int port;
        private String action;

        public WorkerConnectionHandler(int port) {
            this.port = port;
        }

        public void run() {
            try (ServerSocket workerServerSocket = new ServerSocket(port)) {
                while (true) {
                    Socket workerSocket = workerServerSocket.accept();

                    // Handle the worker connection
                    ObjectInputStream workerIn = new ObjectInputStream(workerSocket.getInputStream());
                    this.action = (String) workerIn.readObject();

                    switch (action) {
                        case "show":
                            WorkerDataHandler <List<Booking>> showBookingsHandler = new WorkerDataHandler<>(workerIn);
                            showBookingsHandler.start();
                            break;
                        case "query":
                            WorkerDataHandler <HashMap<String, Integer>> queryHandler = new WorkerDataHandler<>(workerIn);
                            queryHandler.start();
                            break;
                        case "search":
                            WorkerDataHandler <List<Room>> searchHandler = new WorkerDataHandler<>(workerIn);
                            searchHandler.start();
                            break;
                        case "get_user_bookings":
                            WorkerDataHandler <List<Booking>> getUserBookingsHandler = new WorkerDataHandler<>(workerIn);
                            getUserBookingsHandler.start();
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class WorkerDataHandler<T> extends Thread {
        private final ObjectInputStream workerIn;

        public WorkerDataHandler(ObjectInputStream workerIn) {
            this.workerIn = workerIn;
        }

        public void run() {
            try {
                String type = (String) workerIn.readObject();

                if (type.equals("list")) {
                    Pair<Integer, List<T>> intermediateResults = (Pair<Integer, List<T>>) workerIn.readObject();

                    // Add the intermediate results to the list
                    synchronized (intermediateResultsList) {
                        for (T data : intermediateResults.getValue()) {
                            intermediateResultsList.add(new Pair<>(intermediateResults.getKey(), data));
                        }
                        if (intermediateResultsList.isEmpty()) {
                            intermediateResultsList.add(new Pair<>(intermediateResults.getKey(), null));
                        }
                        waitForWorkers();
                    }
                } else {
                    Pair<Integer, HashMap<String, Integer>> intermediateResults = (Pair<Integer, HashMap<String, Integer>>) workerIn.readObject();

                    // Add the intermediate results to the list
                    synchronized (intermediateResultsList) {
                        intermediateResultsList.add(new Pair<>(intermediateResults.getKey(), intermediateResults.getValue()));
                        waitForWorkers();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    Synchronized method to wait for all workers to finish and then notify the reducer's main thread to call the reduce method
    and then send the aggregated results to the Master
    */
    private void waitForWorkers() {
        synchronized (this) {
            workersFinished++;
            if (workersFinished == NUM_WORKERS) {
                allWorkersFinished = true; // All workers have finished
                notifyAll(); // Notify Reducer Thread to proceed
            }
        }
    }

    protected <T> void sendToMaster(Pair<Integer, T> finalResults, String action) {
        try {
            try (Socket masterConnectionSocket = new Socket(masterHost, masterConnectionPort)) {
                // Send final results to master
                ObjectOutputStream out = new ObjectOutputStream(masterConnectionSocket.getOutputStream());
                out.writeObject(action);
                out.writeObject(finalResults);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO MAKE THEM MORE GENERIC -> ONE MAP FOR ALL ACTIONS
    // Map function for show bookings
    public static List<Booking> mapShowBookings(Room room, Manager manager) {
        if(room.getManager().getManagerId() == manager.getManagerId()) {
            return room.getBookings();
        }
        return null;
    }

    // Map function for aggregate query
    public static HashMap<String, Integer> mapQuery(Room room, DateRange dateRange) {
        HashMap<String, Integer> intermediateResults = new HashMap<>();
        String area = room.getArea();

        // Initialize with 0 for the area
        intermediateResults.put(area, 0);

        for (Booking booking : room.getBookings()) {
            DateRange bookingDateRange = booking.getDateRange();
            if (bookingDateRange.isInside(dateRange)) {
                intermediateResults.put(area, intermediateResults.get(area) + 1);
            }
        }

        return intermediateResults;
    }

    // Map function for search
    public static List<Room> mapSearch(Room room, SearchFilter filter) {
        List<Room> intermediateResults = new ArrayList<>();
        if (room.meetsCriteria(filter)) {
            intermediateResults.add(room);
        }
        return intermediateResults;
    }

    // Map function for get user bookings
    public static List<Booking> mapGetUserBookings(Room room, int userId) {
        List<Booking> intermediateResults = new ArrayList<>();
        for (Booking booking : room.getBookings()) {
            if (booking.getUserData().getUserId() == userId) {
                intermediateResults.add(booking);
            }
        }
        return intermediateResults;
    }

    public static synchronized <T> Pair<Integer, T> reduce(List<Pair<Integer, T>> intermediateResults) {
        if (intermediateResults == null || intermediateResults.isEmpty()) {
            throw new IllegalArgumentException("The intermediate results list cannot be null or empty.");
        }

        T firstElement = intermediateResults.getFirst().getValue();

        if (firstElement instanceof Map) {
            Map<String, Integer> finalResults = new HashMap<>();

            for (Pair<Integer, T> pair : intermediateResults) {
                @SuppressWarnings("unchecked")
                Map<String, Integer> intermediateResult = (Map<String, Integer>) pair.getValue();
                for (String area : intermediateResult.keySet()) {
                    finalResults.put(area, finalResults.getOrDefault(area, 0) + intermediateResult.get(area));
                }
            }

            @SuppressWarnings("unchecked")
            T result = (T) finalResults;
            return new Pair<>(intermediateResults.getFirst().getKey(), result);

        } else {
            List<T> finalResults = new ArrayList<>();

            for (Pair<Integer, T> pair : intermediateResults) {
                T data = pair.getValue();
                if (data != null) {
                    finalResults.add(data);
                }
            }

            @SuppressWarnings("unchecked")
            T result = (T) finalResults;
            return new Pair<>(intermediateResults.getFirst().getKey(), result);
        }
    }

    public static void main(String[] args) {
        Reducer reducer = new Reducer();
        reducer.start();
    }
}
