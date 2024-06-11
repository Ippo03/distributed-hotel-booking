package org.backend.master;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.backend.connector.TransmissionObject;
import org.backend.connector.TransmissionObjectBuilder;
import org.backend.connector.TransmissionObjectType;
import org.backend.misc.Authentication;
import org.backend.misc.DateRange;
import org.backend.misc.Pair;
import org.backend.modules.Booking;
import org.backend.modules.Manager;
import org.backend.modules.Review;
import org.backend.modules.Room;
import org.backend.user.UserData;
import org.backend.worker.Worker;

import java.io.*;
import java.net.*;
import java.util.*;

import static org.backend.misc.Util.HEARTBEAT_INTERVAL;

public class Master {
    private WorkerListener workerListener;
    private ManagerListener managerListener;
    private UserListener userListener;
    private ReducerListener reducerListener;
    private HeartbeatListener heartbeatListener;

    private int NUM_WORKERS;
    private int workerConnectionPort, managerConnectionPort, userConnectionPort, reducerConnectionPort, heartbeatPort;
    private int bookingResponsePort;

    Master() {}

    // Initializes server
    public void initServer() {
        try {
            // Initialize configurations
            initMasterConfig();

            // Initialize listeners
            workerListener = new WorkerListener(new ServerSocket(workerConnectionPort));
            managerListener = new ManagerListener(new ServerSocket(managerConnectionPort));
            userListener = new UserListener(new ServerSocket(userConnectionPort));
            reducerListener = new ReducerListener(new ServerSocket(reducerConnectionPort));
            heartbeatListener = new HeartbeatListener(new ServerSocket(heartbeatPort));

            // Start worker listener
            workerListener.start();

            synchronized (workerListener) {
                while (workerListener.getWorkerCount() < NUM_WORKERS) {
                    System.err.println("Master: Waiting for workers to connect...");
                    workerListener.wait();
                }
            }
            System.err.println("Master: All workers connected!!!\n");

            // Start other listeners
            managerListener.start();
            userListener.start();
            reducerListener.start();
            heartbeatListener.start();

            startHeartbeatChecker();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initMasterConfig() {
        // Read the configuration file (.properties)
        FileReader reader = null;
        try {
            reader = new FileReader(STR."\{System.getProperty("user.dir")}\\src\\main\\java\\org\\backend\\master\\data\\masterConfig.properties");
            Properties properties = new Properties();
            properties.load(reader);

            // Read the master configuration file
            NUM_WORKERS = Integer.parseInt(properties.getProperty("numWorkers"));
            workerConnectionPort = Integer.parseInt(properties.getProperty("workerConnectionPort"));
            managerConnectionPort = Integer.parseInt(properties.getProperty("managerConnectionPort"));
            userConnectionPort = Integer.parseInt(properties.getProperty("userConnectionPort"));
            reducerConnectionPort = Integer.parseInt(properties.getProperty("reducerConnectionPort"));
            bookingResponsePort = Integer.parseInt(properties.getProperty("bookingResponsePort"));
            heartbeatPort = Integer.parseInt(properties.getProperty("heartbeatPort"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.err.println("-------------------------------------------------");
        System.err.println("Master Configurations Initialized Successfully");
        System.err.println(STR."Number of Workers: \{NUM_WORKERS}");
        System.err.println(STR."Worker Connection Port: \{workerConnectionPort}");
        System.err.println(STR."Manager Connection Port: \{managerConnectionPort}");
        System.err.println(STR."User Connection Port: \{userConnectionPort}");
        System.err.println(STR."Reducer Connection Port: \{reducerConnectionPort}");
        System.err.println(STR."Booking Response Port: \{bookingResponsePort}");
        System.err.println(STR."Heartbeat Port: \{heartbeatPort}");
        System.err.println("-------------------------------------------------");
    }

    // Send the room to workers with hashing the room name
    private class HashTask extends Thread {
        List<Worker.WorkerTask> tasksForHashing;

        public HashTask(List<Worker.WorkerTask> tasksForHashing) {
            this.tasksForHashing = tasksForHashing;
        }

        @Override
        public void run() {
            try {
                // Send tasks to workers with hash function
                for (Worker.WorkerTask task : tasksForHashing) {
                    int hash = task.getTask().hashCode() % NUM_WORKERS;
                    int backupHash= 0;
                    ObjectOutputStream out = workerListener.workerThreads.get(hash).out;
                    out.writeObject(task);
                    out.flush();
                }
                System.err.println("Master: All tasks sent to workers");
            } catch (Exception e) {
                System.err.println("Master: Error sending tasks to workers");
            }
        }
    }

    private class WorkerListener extends Thread {
        private final ServerSocket workerServerSocket;
        private final ArrayList<WorkerHandlerThread> workerThreads;

        WorkerListener(ServerSocket workerServerSocket) {
            this.workerServerSocket = workerServerSocket;
            this.workerThreads = new ArrayList<>();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket workerSocket = workerServerSocket.accept();
                    System.err.println("Worker connected");

                    WorkerHandlerThread workerThread = new WorkerHandlerThread(workerSocket);
                    workerThread.start();

                    synchronized (this) {
                        workerThreads.add(workerThread);
                        if (workerThreads.size() == NUM_WORKERS) {
                            notifyAll(); // Notify that NUM_WORKERS have connected
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public WorkerHandlerThread getWorkerThread(int workerId) {
            return workerThreads.get(workerId);
        }

        public synchronized int getWorkerCount() {
            return workerThreads.size();
        }

    }

//    public void reassignTasks(int failedWorkerId) {
//        // Iterate over all tasks
//        for (Worker.WorkerTask task : tasksForHashing) {
//            // Check if the task was assigned to the failed worker
//            if (task.getTask().hashCode() % NUM_WORKERS == failedWorkerId) {
//                // Reassign the task to a new worker
//                int newWorkerId = task.getTask().hashCode() % (NUM_WORKERS - 1);
//                workerListener.getWorkerThread(newWorkerId).sendTask(task);
//            }
//        }
//    }

    public static class WorkerHandlerThread extends Thread {
        private final Socket workerSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        private WorkerHandlerThread(Socket workerSocket) {
            this.workerSocket = workerSocket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(workerSocket.getOutputStream());
                in = new ObjectInputStream(workerSocket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class ManagerListener extends Thread {
        private final ServerSocket managerServerSocket;
        private final ArrayList<ManagerHandlerThread> managerThreads;

        ManagerListener(ServerSocket managerServerSocket) {
            this.managerServerSocket = managerServerSocket;
            this.managerThreads = new ArrayList<>();
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Socket managerSocket = managerServerSocket.accept();
                    System.err.println("Manager connected");

                    ManagerHandlerThread managerThread = new ManagerHandlerThread(managerSocket);
                    managerThread.start();

                    synchronized(managerThreads) {
                        managerThreads.add(managerThread);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class ManagerHandlerThread extends Thread {
        private final Socket managerSocket;
        private int managerId;
        private ObjectOutputStream out;

        private ManagerHandlerThread(Socket managerSocket) {
            this.managerSocket = managerSocket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(managerSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(managerSocket.getInputStream());

                while (true) {
                    System.err.println("ManagerHandlerThread: Waiting for token");

                    String receivedToken = (String) in.readObject();
                    System.err.println(STR."ManagerHandlerThread: Received token: \{receivedToken}");

                    switch (receivedToken) {
                        case "init":
                            System.err.println("ManagerHandlerThread: Manager initializes all rooms");
                            List<Room> rooms = (List<Room>) in.readObject();

                            List<Worker.WorkerTask> initRoomsTasks = new ArrayList<>();
                            for (Room room : rooms) {
                                initRoomsTasks.add(new Worker.AddRoomTask(room));
                            }

                            HashTask hashTask = new HashTask(initRoomsTasks);
                            hashTask.start();
                            break;
                        case "add":
                            System.err.println("ManagerHandlerThread: Manager adds a new room");
                            Room room = (Room) in.readObject();

                            List<Worker.WorkerTask> addRoomTasks = new ArrayList<>();
                            addRoomTasks.add(new Worker.AddRoomTask(room));

                            HashTask addHashTask = new HashTask(addRoomTasks);
                            addHashTask.start();
                            break;
                        case "show":
                            System.err.println("ManagerHandlerThread: Manager is shown their bookings");
                            Manager managerShow = (Manager) in.readObject();
                            setManagerId(managerShow.getManagerId());

                            Worker.ShowBookingsTask showBookingsTask = new Worker.ShowBookingsTask(managerShow);

                            // send showTask to all workers
                            synchronized (workerListener.workerThreads) {
                                for (WorkerHandlerThread workerHandlerThread : workerListener.workerThreads) {
                                    try {
                                        ObjectOutputStream workerOut = workerHandlerThread.out;
                                        workerOut.writeObject(showBookingsTask);
                                        workerOut.flush();
                                    } catch (IOException e) {
                                        System.err.println("Master: Error sending show bookings task to worker");
                                    }
                                }
                            }
                            break;
                        case "query":
                            System.err.println("ManagerHandlerThread: Manager is shown aggregated bookings by area");
                            Manager managerQuery = (Manager) in.readObject();
                            setManagerId(managerQuery.getManagerId());
                            DateRange dateRange = (DateRange) in.readObject();

                            Worker.AggregatedQueryTask aggregatedQueryTask = new Worker.AggregatedQueryTask(managerQuery, dateRange);

                            // send queryTask to all workers
                            synchronized (workerListener.workerThreads) {
                                for (WorkerHandlerThread workerHandlerThread : workerListener.workerThreads) {
                                    ObjectOutputStream workerOut = workerHandlerThread.out;
                                    workerOut.writeObject(aggregatedQueryTask);
                                    workerOut.flush();
                                }
                            }
                            break;
                        default:
                            System.err.println(STR."ManagerHandlerThread: Unknown request token: \{receivedToken}");
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        void setManagerId(int managerId) {
            this.managerId = managerId;
        }
    }

    private class UserListener extends Thread {
        private final ServerSocket userServerSocket;
        private final ArrayList<UserHandlerThread> userThreads;

        UserListener(ServerSocket userServerSocket) {
            this.userServerSocket = userServerSocket;
            this.userThreads = new ArrayList<>();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket userSocket = userServerSocket.accept();
                    System.err.println("User connected");

                    UserHandlerThread userThread = new UserHandlerThread(userSocket);
                    userThread.start();

                    synchronized (userThreads) {
                        userThreads.add(userThread);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public class UserHandlerThread extends Thread {
        private final Socket userSocket;
        private UserData userData;

        private ObjectOutputStream out;
        private ObjectInputStream in;

        private UserHandlerThread(Socket userSocket) {
            this.userSocket = userSocket;
        }

        @Override
        public void run() {
            try {
                boolean loggedIn = false;
                out = new ObjectOutputStream(userSocket.getOutputStream());
                in = new ObjectInputStream(userSocket.getInputStream());

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("yyyy-MM-dd");
                Gson gson = gsonBuilder.create();

                Authentication auth = new Authentication();

                while (true) {
                    System.err.println("UserHandlerThread: Waiting for token");
                    String receivedJson = (String) in.readObject();
                    TransmissionObject receivedData = gson.fromJson(receivedJson, TransmissionObject.class);
                    System.err.println(STR."UserHandlerThread: Received token: \{receivedData.type}");

                    switch (receivedData.type) {
                        case TransmissionObjectType.LOGIN:
                            System.err.println("UserHandlerThread: User login");

                            int userId = auth.login(receivedData.username, receivedData.password);

                            if (userId == -1) {
                                loginFailure("Login failed");
                                break;
                            }

                            String username = receivedData.username;
                            String profilePicture = Authentication.getProfilePicture(username);
                            setUserData(userId, username, profilePicture);

                            System.err.println(STR."User \{receivedData.username} with #\{userId} logged in successfully");

                            loggedIn = true;

                            TransmissionObject transmissionObject = new TransmissionObjectBuilder()
                                    .type(TransmissionObjectType.USERDATA)
                                    .userData(userData)
                                    .success(1)
                                    .build();

                            out.writeObject(gson.toJson(transmissionObject));
                            out.flush();
                            break;
                        case TransmissionObjectType.LOGOUT:
                            if (loggedIn) {
                                TransmissionObject to = new TransmissionObjectBuilder()
                                        .type(TransmissionObjectType.LOGOUT)
                                        .success(1)
                                        .build();

                                out.writeObject(gson.toJson(to));
                                out.flush();
                                loggedIn = false;
                                System.err.println(STR."User with name \{receivedData.username} logged out succesfully");
                            }
                            break;
                        case TransmissionObjectType.SEARCH:
                            if (loggedIn) {
                                System.err.println(STR."UserHandlerThread: User #\{userData.getUserId()} searches for rooms");

                                // Create a SearchFilterTask
                                Worker.SearchFilterTask searchFilterTask = new Worker.SearchFilterTask(new Pair<>(userData.getUserId(), receivedData.searchFilter));

                                // Send SearchFilterTask to all workers
                                for (WorkerHandlerThread workerHandlerThread : workerListener.workerThreads) {
                                    try {
                                        ObjectOutputStream workerOut = workerHandlerThread.out;
                                        workerOut.writeObject(searchFilterTask);
                                        workerOut.flush();
                                    } catch (IOException e) {
                                        System.err.println("Master: Error sending search filter task to worker");
                                    }
                                }
                            }
                            break;
                        case TransmissionObjectType.REVIEW:
                            if (loggedIn) {
                                Review review = receivedData.review;
                                DateRange bookingDates = receivedData.bookingDates;
                                String action = receivedData.message;
                                int hash = review.getRoomInfo().hashCode() % NUM_WORKERS;

                                System.err.println(STR."UserHandlerThread: User #\{userData.getUserId()} reviews room (\{review.getRoomInfo().getRoomName()})");

                                // Decide the message to toast based on the action
                                String messageToToast = switch (action) {
                                    case "NEW" -> "Review submitted successfully !";
                                    case "UPDATE" -> "Review updated successfully !";
                                    case "DELETE" -> "Review deleted successfully !";
                                    default -> "";
                                };

                                Worker.ReviewTask reviewTask = new Worker.ReviewTask(review, bookingDates, action);

                                ObjectOutputStream workerOut = workerListener.workerThreads.get(hash).out;
                                workerOut.writeObject(reviewTask);
                                workerOut.flush();

                                TransmissionObject transmissionObjectReview = new TransmissionObjectBuilder()
                                        .type(TransmissionObjectType.REVIEW_RESULT)
                                        .message(messageToToast)
                                        .success(1)
                                        .build();

                                out.writeObject(gson.toJson(transmissionObjectReview));
                                out.flush();
                            }
                            break;
                        case TransmissionObjectType.BOOK:
                            ServerSocket userBookingResponseSocket = new ServerSocket(bookingResponsePort);
                            String bookingResult="";

                            if (loggedIn) {
                                Booking booking = receivedData.booking;
                                int hash = booking.getRoomInfo().hashCode() % NUM_WORKERS;

                                System.err.println(STR."UserHandlerThread: User #\{userData.getUserId()} tries to book room (\{booking.getRoomInfo().getRoomName()})");

                                // Create a BookingTask
                                Worker.BookingTask bookingTask = new Worker.BookingTask(booking);

                                // Send BookingTask to the worker that has the room
                                ObjectOutputStream workerOut = workerListener.workerThreads.get(hash).out;
                                workerOut.writeObject(bookingTask);
                                workerOut.flush();

                                // Open Server Socket to receive the booking result from the worker
                                try{
                                    Socket userBookingResponseConnection = userBookingResponseSocket.accept();

                                    ObjectInputStream bookResponseIn = new ObjectInputStream(userBookingResponseConnection.getInputStream());
                                    bookingResult = (String) bookResponseIn.readObject();
                                } catch (Exception e){
                                    System.err.println(e.getClass());
                                    System.err.println("UserHandlerThread: Error receiving booking result from the worker");
                                }

                                // Declare a new TransmissionObject for the booking result
                                TransmissionObject transmissionObjectBooking;

                                // Decide the message based on the booking result
                                if ("SUCCESS".equals(bookingResult)) {
                                    transmissionObjectBooking = new TransmissionObjectBuilder()
                                            .type(TransmissionObjectType.BOOK_RESULT)
                                            .message("Booking completed successfully")
                                            .success(1) // 1 -> success
                                            .build();
                                } else {
                                    transmissionObjectBooking = new TransmissionObjectBuilder()
                                            .type(TransmissionObjectType.BOOK_RESULT)
                                            .message("Booking failed")
                                            .success(0) // 0 -> failed
                                            .build();
                                }

                                out.writeObject(gson.toJson(transmissionObjectBooking));
                                out.flush();
                                userBookingResponseSocket.close();
                                break;
                            }
                        case GET_USER_BOOKINGS:
                            if (loggedIn) {
                                System.err.println(STR."UserHandlerThread: User #\{userData.getUserId()} gets their bookings");

                                // Create a GetUserBookingsTask
                                Worker.GetUserBookingsTask getBookingsTask = new Worker.GetUserBookingsTask(userData.getUserId());

                                // Send GetUserBookingsTask to all workers
                                for (WorkerHandlerThread workerHandlerThread : workerListener.workerThreads) {
                                    try {
                                        ObjectOutputStream workerOut = workerHandlerThread.out;
                                        workerOut.writeObject(getBookingsTask);
                                        workerOut.flush();
                                    } catch (IOException e) {
                                        System.err.println("Master: Error sending get user bookings task to worker");
                                    }
                                }
                            break;
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            private void setUserData(int userId, String username, String profilePicture) {
                this.userData = new UserData(userId);
                userData.setUsername(username);
                userData.setProfilePicture(profilePicture);
            }

            private void loginFailure(String message) {
            try {
                Gson gson = new Gson();
                TransmissionObject transmissionObject = new TransmissionObjectBuilder()
                        .type(TransmissionObjectType.USERDATA)
                        .message(message)
                        .success(0)
                        .build();

                System.err.println("UserHandlerThread: User login failed");

                out.writeObject(gson.toJson(transmissionObject));
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class ReducerListener extends Thread {
        private final ServerSocket reducerServerSocket;

        private ReducerListener(ServerSocket reducerServerSocket) {
            this.reducerServerSocket = reducerServerSocket;
        }

        public void run() {
            try {
                while (true) {
                    Socket reducerSocket = reducerServerSocket.accept();
                    System.err.println("Reducer connected");

                    ObjectOutputStream out = new ObjectOutputStream(reducerSocket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(reducerSocket.getInputStream());

                    String reducerCode = (String) in.readObject();

                    Pair<Integer, List<Object>> finalResults = (Pair<Integer, List<Object>>) in.readObject();
                    System.err.println(STR."ReducerHandler: Received final results from reducer: \{finalResults}");

                    // Send the final results to the client (user or manager)
                    sendToClient(finalResults, reducerCode);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> void sendToClient(Pair<Integer, T> finalResults, String taskFlag) {
        UserHandlerThread userThread;
        ObjectOutputStream userOut;

        ManagerHandlerThread managerThread;
        ObjectOutputStream managerOut;

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd");
            Gson gson = gsonBuilder.create();

            switch (taskFlag) {
                case "show", "query":
                    managerThread = managerListener.managerThreads.stream()
                            .filter(thread -> thread.managerId == finalResults.getKey())
                            .findFirst()
                            .orElse(null);
                    managerOut = managerThread.out;

                    managerOut.writeObject(finalResults.getValue());
                    managerOut.flush();
                    break;
                case "search":
                    TransmissionObject searchResultTransmissionObject = new TransmissionObjectBuilder()
                            .type(TransmissionObjectType.SEARCH_RESULT)
                            .rooms((List<Room>) finalResults.getValue())
                            .success(1)
                            .build();
                    sendTransmissionObjectToUser(finalResults.getKey(), gson, searchResultTransmissionObject);
                    break;

                case "get_user_bookings":
                    TransmissionObject userBookingsTransmissionObject = new TransmissionObjectBuilder()
                            .type(TransmissionObjectType.GET_USER_BOOKINGS)
                            .userBookings((List<Booking>) finalResults.getValue())
                            .success(1)
                            .build();
                    sendTransmissionObjectToUser(finalResults.getKey(), gson, userBookingsTransmissionObject);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendTransmissionObjectToUser(int userId, Gson gson, TransmissionObject transmissionObject) throws IOException {
        UserHandlerThread userThread = userListener.userThreads.stream()
                .filter(thread -> thread.userData.getUserId() == userId)
                .findFirst()
                .orElse(null);

        if (userThread != null) {
            ObjectOutputStream userOut = userThread.out;
            userOut.writeObject(gson.toJson(transmissionObject));
            userOut.flush();
        }
    }

    private static class HeartbeatListener extends Thread {
        private final ServerSocket heartbeatServerSocket;
        private final Map<Integer, Long> workerHeartbeatMap;

        public HeartbeatListener(ServerSocket heartbeatServerSocket) {
            this.heartbeatServerSocket = heartbeatServerSocket;
            this.workerHeartbeatMap = new HashMap<>();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket workerSocket = heartbeatServerSocket.accept();
                    new HeartbeatHandler(workerSocket).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private class HeartbeatHandler extends Thread {
            private final Socket workerSocket;

            public HeartbeatHandler(Socket workerSocket) {
                this.workerSocket = workerSocket;
            }

            @Override
            public void run() {
                int workerId = 0;
                try {
                    ObjectInputStream in = new ObjectInputStream(workerSocket.getInputStream());
                    while (true) {
                        workerId = (Integer) in.readObject();
                        synchronized (workerHeartbeatMap) {
                            workerHeartbeatMap.put(workerId, System.currentTimeMillis());
                        }
                    }
                } catch (EOFException e) {

                } catch (SocketException e) { // When worker connection is terminated
                    System.err.println(STR."HeartbeatHandler: Worker #\{workerId}  disconnected");
                    // Remove the worker from the map
                    synchronized (workerHeartbeatMap) {
                        workerHeartbeatMap.remove(workerId);
                    }
                }catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void checkHeartbeats() {
            long currentTime = System.currentTimeMillis();
            synchronized (workerHeartbeatMap) {
                workerHeartbeatMap.entrySet().removeIf(entry -> {
                    if (currentTime - entry.getValue() > HEARTBEAT_INTERVAL) {
                        System.err.println(STR."Worker \{entry.getKey()} has timed out");
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    private void startHeartbeatChecker() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(HEARTBEAT_INTERVAL);
                    heartbeatListener.checkHeartbeats();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        Master master = new Master();
        master.initServer();
    }
}
