package org.backend.manager;

import org.backend.misc.CommandInput;
import org.backend.misc.DateRange;
import org.backend.misc.JsonParser;
import org.backend.modules.Manager;
import org.backend.modules.Room;
import org.backend.modules.Booking;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ManagerConsoleApp {
    private  String masterHost;
    private int masterConnectionPort;
    private final Manager manager;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ManagerConsoleApp(int managerId) {
        this.manager = new Manager(managerId);
    }

    public void run() {
        try {
            // Initialize the manager configuration
            initManagerConfig();

            // Connect to the server
            socket = new Socket(masterHost, masterConnectionPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Prompt the user to load initial rooms
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\nPress 'Y' to load all initial rooms: ");
                String input = scanner.next();
                if ("Y".equalsIgnoreCase(input)) {
                    loadInitialRooms();
                    break;
                } else {
                    System.out.println("Invalid input. Please press 'Y' to load all initial rooms.");
                }
            }

            // Start manager interaction loop
            interactWithManager();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle connection errors here
        } finally {
            // Close the socket and streams when done
            closeConnection();
        }
    }

    private void initManagerConfig() {
        FileReader reader = null;
        try {
            reader = new FileReader(STR."\{System.getProperty("user.dir")}\\src\\main\\java\\org\\backend\\manager\\data\\managerConfig.properties");
            Properties properties = new Properties();
            properties.load(reader);

            masterHost = (String) properties.getProperty("masterHost");
            masterConnectionPort = Integer.parseInt(properties.getProperty("masterConnectionPort"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadInitialRooms() {
        try {
            // Parse json file
            JsonParser jsonParser = new JsonParser();
            List<Room> rooms = jsonParser.roomsFromJson("src/main/java/org/backend/manager/data/rooms/all-rooms.json");

            // Set manager for each room
            for (Room room : rooms) {
                room.setManager(manager);
            }

            // Send the list of rooms to the server to initialize
            out.writeObject("init");
            out.writeObject(rooms);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void interactWithManager() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Display the main menu for the selected manager
                System.out.println("\nManager Console App :");
                System.out.println("1. Add a new room");
                System.out.println("2. Show my bookings");
                System.out.println("3. Show bookings of a date range");
                System.out.println("4. Exit");
                System.out.print("\nEnter the number of the action you want to perform: ");

                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                    continue;
                }

                switch (choice) {
                    case 1:
                        // Add a new room
                        System.out.print("Enter the filename of the room to add: ");
                        scanner.nextLine();
                        String filename = scanner.nextLine();

                        // Read the JSON file and create a Room object
                        JsonParser jp = new JsonParser();
                        List<Room> rooms = jp.roomsFromJson(filename);
                        for (Room room : rooms) {
                            room.setManager(manager);
                        }

                        // Send the room to be added
                        out.writeObject("add");
                        out.writeObject(rooms.get(0));
                        out.flush();
                        break;
                    case 2:
                        // Send the manager to get the bookings
                        out.writeObject("show");
                        out.writeObject(manager);
                        out.flush();

                        // Receive bookings from the server
                        List<Booking> bookings = (List<Booking>) in.readObject();
                        if (bookings.isEmpty()) {
                            System.out.println("\nNo bookings found.");
                        } else {
                            System.out.println("\nYour bookings are:");
                            for (Booking booking : bookings) {
                                System.out.println(booking);
                            }
                        }
                        break;
                    case 3:
                        // Prompt the user to enter the date range
                        DateRange dateRange = CommandInput.inputDateRange();

                        // Send the manager and date range to get the aggregated bookings
                        out.writeObject("query");
                        out.writeObject(manager);
                        out.writeObject(dateRange);
                        out.flush();

                        HashMap<String, Integer> aggregatedBookings = (HashMap<String, Integer>) in.readObject();

                        // Display the aggregated bookings
                        System.out.println("\nAggregated bookings for the date range (" + dateRange.toRangeString() + ") are:");
                        for (Map.Entry<String, Integer> entry : aggregatedBookings.entrySet()) {
                            System.out.println(entry.getKey() + ": " + entry.getValue());
                        }
                        break;
                    case 4:
                        // Exit
                        System.out.println("Exiting the console application...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Prompt the manager id and master host from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the manager id: ");
        int managerId = scanner.nextInt();

        // Start the manager console application
        ManagerConsoleApp managerConsoleApp = new ManagerConsoleApp(managerId);
        managerConsoleApp.run();
    }
}
