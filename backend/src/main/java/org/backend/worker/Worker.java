package org.backend.worker;

import org.backend.mapreduce.Reducer;
import org.backend.misc.DateRange;
import org.backend.misc.Pair;
import org.backend.misc.SearchFilter;
import org.backend.modules.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.util.*;

import static org.backend.mapreduce.Reducer.mapQuery;
import static org.backend.mapreduce.Reducer.mapSearch;
import static org.backend.misc.Util.HEARTBEAT_INTERVAL;

public class Worker extends Thread{
    private final int wid;
    Socket masterConnectionSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String masterHost, reducerHost;
    private int masterConnectionPort, reducerPort;
    private static int bookResponsePort;
    private int heartbeatPort;
    protected List<Room> rooms;
    protected List<Room> backupRooms; // Worker with id 0 will have a backup of all rooms. King Worker !


    public Worker(int wid) {
        this.wid = wid;
        this.out = null;
        this.in = null;
        this.rooms = new ArrayList<>();
        this.backupRooms = new ArrayList<>();
    }

    public void run() {
        masterConnectionSocket = null;
        try {
            // Initialize the worker configuration
            initWorkerConfig();

            // Connect to the master
            masterConnectionSocket = new Socket(masterHost, masterConnectionPort);
            System.err.println(STR."Worker \{this.wid} connected to the master");

            // Initialize the input and output streams
            out = new ObjectOutputStream(masterConnectionSocket.getOutputStream());
            in = new ObjectInputStream(masterConnectionSocket.getInputStream());

            // Start the heartbeat thread
            new Thread(this::sendHeartbeat).start();

            while (true) {
                // Read the message from the master
                Object message = in.readObject();

                // Executes the task using polymorphism
                WorkerTask task = (WorkerTask) message;
                task.execute(this);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendHeartbeat() {
        while (true) {
            try (Socket heartbeatSocket = new Socket(masterHost, heartbeatPort);
                 ObjectOutputStream heartbeatOut = new ObjectOutputStream(heartbeatSocket.getOutputStream())) {

                heartbeatOut.writeObject(this.wid); // Send worker ID as heartbeat message
                heartbeatOut.flush();

                Thread.sleep(HEARTBEAT_INTERVAL);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initWorkerConfig() {
        FileReader reader = null;
        try {
            reader = new FileReader(STR."\{System.getProperty("user.dir")}\\src\\main\\java\\org\\backend\\worker\\data\\workerConfig.properties");
            Properties properties = new Properties();
            properties.load(reader);

            // Read the worker configuration file
            this.masterConnectionPort = Integer.parseInt(properties.getProperty("masterConnectionPort"));
            bookResponsePort = Integer.parseInt(properties.getProperty("bookResponsePort"));
            this.reducerPort = Integer.parseInt(properties.getProperty("reducerPort"));
            this.heartbeatPort = Integer.parseInt(properties.getProperty("heartbeatPort"));
            this.masterHost = (String) properties.getProperty("masterHost");
            this.reducerHost = (String) properties.getProperty("reducerHost");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface WorkerTask {
        void execute(Worker worker);
        Object getTask();
    }

    public static class AddRoomTask implements WorkerTask, Serializable {
        private final Room room;

        public AddRoomTask(Room room) {
            this.room = room;
        }

        @Override
        public void execute(Worker worker) {
            worker.rooms.add(room);
            System.err.println(STR."Worker #\{worker.wid} assigned room \{room.getRoomName()} to its list");
            if (worker.wid == 0){
                worker.backupRooms.add(room);
                System.err.println(STR."Worker #\{worker.wid} assigned room \{room.getRoomName()} to the backup list");
            }
        }

        @Override
        public Object getTask() {
            return room;
        }
    }

    public static class ShowBookingsTask implements WorkerTask, Serializable {
        private final Manager manager;

        public ShowBookingsTask(Manager manager) {
            this.manager = manager;
        }

        @Override
        public void execute(Worker worker) {
            List<Booking> showBookingsIntermediateResults = new ArrayList<>();

            // Apply the Map operation
            for (Room room : worker.rooms) {
                showBookingsIntermediateResults.addAll(Objects.requireNonNull(Reducer.mapShowBookings(room, manager)));
            }

            Pair <Integer, List<Booking>> intermediateShowBookingsList = new Pair<>(manager.getManagerId() , showBookingsIntermediateResults);

            // Send the intermediate results and user ID to the Reducer
            sendIntermediateResultsToReducer(worker, "show", "list", intermediateShowBookingsList, "show bookings");
        }

        @Override
        public Object getTask() {
            return manager;
        }
    }

    public static class AggregatedQueryTask implements WorkerTask, Serializable {
        private final Manager manager;
        private final DateRange dateRange;

        public AggregatedQueryTask(Manager manager, DateRange dateRange) {
            this.manager = manager;
            this.dateRange = dateRange;
        }

        @Override
        public void execute(Worker worker) {
            HashMap<String, Integer> aggregatedQueryIntermediateResults = new HashMap<>();

            // Apply the Map operation
            for (Room room : worker.rooms) {
                HashMap<String, Integer> roomResults = mapQuery(room, dateRange);
                for (String area : roomResults.keySet()) {
                    aggregatedQueryIntermediateResults.put(area, aggregatedQueryIntermediateResults.getOrDefault(area, 0) + roomResults.get(area));
                }
            }

            // Send the intermediate results and user ID to the Reducer
            Pair<Integer, HashMap<String, Integer>> intermediateAggregatedQueryMap = new Pair<>(manager.getManagerId(), aggregatedQueryIntermediateResults);

            // Send the intermediate results to the Reducer
            sendIntermediateResultsToReducer(worker, "query", "map", intermediateAggregatedQueryMap, "aggregated query");
        }

        @Override
        public Object getTask() {
            return dateRange;
        }
    }

    public static class SearchFilterTask implements WorkerTask, Serializable {
        private final Pair<Integer, SearchFilter> searchFilter;

        public SearchFilterTask(Pair<Integer, SearchFilter> searchFilter) {
            this.searchFilter = searchFilter;
        }

        @Override
        public void execute(Worker worker) {
            List<Room> searchIntermediateResults = new ArrayList<>();

            // Apply the Map operation and start worker threads searching
            for (Room room : worker.rooms) {
                searchIntermediateResults.addAll(mapSearch(room, searchFilter.getValue()));
            }

            // Send the intermediate results and user ID to the Reducer
            Pair<Integer, List<Room>> intermediateSearchList = new Pair<>(searchFilter.getKey(), searchIntermediateResults);

            // Send the intermediate results to the Reducer
            sendIntermediateResultsToReducer(worker, "search", "list", intermediateSearchList, "search");
        }

        @Override
        public Object getTask() {
            return searchFilter;
        }
    }

    public static class ReviewTask implements WorkerTask, Serializable {
        private final Review review;
        private final DateRange bookingDates;
        private final String action;

        public ReviewTask(Review review, DateRange bookingDates, String action) {
            this.review = review;
            this.bookingDates = bookingDates;
            this.action = action;
        }

        @Override
        public void execute(Worker worker) {
            System.err.println(STR."Worker #\{worker.wid} is reviewing room: \{review.getRoomInfo().getRoomName()}");
            worker.reviewRoom(Objects.requireNonNull(worker.findRoom(review.getRoomInfo().getRoomId())), review, bookingDates, action);
        }

        @Override
        public Object getTask() {
            return review;
        }
    }

    public static class BookingTask implements WorkerTask, Serializable {
        private final Booking booking;

        public BookingTask(Booking booking) {
            this.booking = booking;
        }

        @Override
        public void execute(Worker worker) {
            System.err.println(STR."Worker #\{worker.wid} is trying to book room (\{booking.getRoomInfo().getRoomName()})");
            String bookingResult = worker.bookRoom(Objects.requireNonNull(worker.findRoom(booking.getRoomInfo().getRoomId())), booking);

            // Send the booking result to the master
            Socket bookSocket;
            ObjectOutputStream bookResponseOut;
            try {
                bookSocket = new Socket(worker.masterHost, bookResponsePort);
                bookResponseOut = new ObjectOutputStream(bookSocket.getOutputStream());
                bookResponseOut.writeObject(bookingResult);
                bookResponseOut.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object getTask() {
            return booking;
        }
    }

    public static class GetUserBookingsTask implements WorkerTask, Serializable {
        int userId;

        public GetUserBookingsTask(int userId){
            this.userId = userId;
        }

        @Override
        public void execute(Worker worker) {
            List<Booking> getUserBookingsIntermediateResults = new ArrayList<>();

            // Apply the Map operation
            for (Room room : worker.rooms) {
                getUserBookingsIntermediateResults.addAll(Reducer.mapGetUserBookings(room, userId));
            }

            // Send the intermediate results and user ID to the Reducer
            Pair<Integer, List<Booking>> intermediateGetUserBookingsList = new Pair<>(userId, getUserBookingsIntermediateResults);

            // Send the intermediate results to the Reducer
            sendIntermediateResultsToReducer(worker, "get_user_bookings", "list", intermediateGetUserBookingsList, "get user bookings");
        }

        @Override
        public Object getTask() {
            return userId;
        }
    }

    public static void sendIntermediateResultsToReducer(Worker worker, String operation, String structure, Pair pairList, String message) {
        try {
            Socket reducerSocket = new Socket(worker.reducerHost, worker.reducerPort);
            ObjectOutputStream out = new ObjectOutputStream(reducerSocket.getOutputStream());

            System.err.printf("Worker #%d sent (%s) intermediate results to the reducer%n", worker.wid, message);

            out.writeObject(operation);
            out.writeObject(structure);
            out.writeObject(pairList);
            out.flush();

            // Close resources
            out.close();
            reducerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Room findRoom(String roomId) {
        for (Room room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    private void reviewRoom(Room room, Review review, DateRange bookingDates, String action) {
        synchronized (room){
            switch (action){
                case "NEW":
                    room.getBookings().stream().filter(booking -> booking.getDateRange().equals(bookingDates)).findFirst().ifPresent(booking -> booking.setReview(review));
                    room.reviewOnCreate(review);
                    break;
                case "UPDATE":
                    Booking oldBooking = room.getBookings().stream().filter(booking -> booking.getDateRange().equals(bookingDates)).findFirst().orElse(null);
                    if (oldBooking != null) {
                        room.reviewOnUpdate(oldBooking.getReview(), review);
                        oldBooking.setReview(review);
                    }
                    break;
                case "DELETE":
                    Booking bookingToDelete = room.getBookings().stream().filter(booking -> booking.getDateRange().equals(bookingDates)).findFirst().orElse(null);
                    if (bookingToDelete != null) {
                        room.reviewOnDelete(bookingToDelete.getReview());
                        bookingToDelete.setReview(null);
                    }
                    break;
            }
            System.err.println(STR."Room \{room.getRoomName()} has a new rating of \{room.getStars()} based on \{room.getNoOfReviews()} reviews");
        }
    }

    private String bookRoom(Room room , Booking booking){
        synchronized (room){
            if(room.book(booking)){
                System.err.println(STR."Room \{room.getRoomName()} has been booked successfully");
                return "SUCCESS";
            }else {
                System.err.println(STR."Room \{room.getRoomName()} has failed to book");
                return "FAILED";
            }
        }
    }

    public static void main(String[] args){
        // Prompt the user to enter the worker ID
        System.out.println("Enter the worker ID: ");
        int workerId = Integer.parseInt(System.console().readLine());

        // Create the worker thread
        Worker worker = new Worker(workerId);
        worker.start();
        // DEBUGGING
//        for (int i = 0; i < 3; i++) {
//            Worker worker = new Worker(i);
//            worker.start();
//        }
    }
}

