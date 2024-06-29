<div align="center">
    <img src="/media/bookaro_logo.png" width="200" >
</div>

## Description 📝

Bookaro is a distributed hotel booking system developed in Java, leveraging the MapReduce framework for efficient data processing. The system comprises a mobile frontend application for managing accommodations and a robust backend system for data analysis, processing, and storage. Bookaro supports functionalities for both managers and renters, providing a comprehensive solution for room rentals.

## Features 🔍

### Manager Functionality
- **Add Accommodations:** Managers can add new accommodations.
- **Set Available Dates:** Define available rental dates for accommodations.
- **View Bookings:** Display bookings for owned accommodations.
- **View Aggregated Bookings:** Managers can view bookings filtered by area for a specified date range.
- **Console Application:** Manage accommodations via a console interface.

### Renter Functionality
- **Filter Accommodations:** Search based on area, dates, number of persons, price, and rating.
- **Book Accommodations:** Renters can book accommodations that meet their criteria.
- **View Bookings:**  Renters can view their bookings.
- **Rate Rooms:** Provide star ratings (1-5) for rented rooms.
- **Android UI:** All functionalities accessible through an Android application.

## Compile and Run

<details>
  <summary>Backend Setup</summary>
    <br>

  1. **Configuration:**  
     Update the hosts in the properties files located in the `/data` folder of each main component (Master, Worker, Reducer, Manager) directory. Ensure that the hostnames or IP addresses are correctly set for your network setup.

  2. **Compile the Java files:**
     ```bash
     javac .\backend\src\main\java\org\backend\master\Master.java
     javac .\backend\src\main\java\org\backend\worker\Worker.java
     javac .\backend\src\main\java\org\backend\mapreduce\Reducer.java
     javac .\backend\src\main\java\org\backend\manager\ManagerConsoleApp.java
     ```

  3. **Run the Java files:**  
    Always in the correct workspace: 

     Start the Master node:  
     ```bash
     java Master
     ```

     Start the Worker nodes (in separate terminals), assigning each a unique ID (e.g., 1, 2, 3):  
     ```bash
     java Worker 1
     java Worker 2
     java Worker 3
     ```

     Start the Reducer node:  
     ```bash
     java Reducer
     ```

     Start the Manager node:  
     ```bash
     java ManagerConsoleApp
     ```

  4. **Using the Manager:**  
     Follow the menu options provided by the Manager to start adding accommodations, managing bookings, and performing other tasks.
</details>
<br>
<details>
  <summary>Frontend Setup</summary>
  <br>

  - Open the Android project in Android Studio.
  - Locate `BackendConnector.java` and update the host of the Master node.
  - Build and run the application on an Android device or emulator.
</details>



## Implementation Details 📘

### Room JSON Structure


This example JSON provides a basic representation of a hotel room object, outlining its attributes such as `roomId`, `roomName`, `noOfPersons`, `area`, `stars`, `noOfReviews`, `price`, `roomImage`, and `availableDates`.

```json
{
    "roomId": "1",
    "roomName": "Deluxe Suite",
    "noOfPersons": 4,
    "area": "Athens",
    "stars": 5,
    "noOfReviews": 12,
    "price": "800",
    "roomImage": "src/main/java/org/backend/images/rooms/deluxe_suite.png",
    "availableDates": [
        {
            "startDate": "2024-05-28",
            "endDate": "2024-06-17"
        }
    ]
}
```

### MapReduce Framework
- **Map Function:** Processes input key/value pairs to produce intermediate key/value pairs.
- **Reduce Function:** Merges intermediate values to produce final results.

### Backend
- **Master Node:** Functions as the central coordinator, handling job distribution, aggregation, and communication.
- **Worker Nodes:** Execute mapping tasks in parallel, processing data according to job assignments.
- **Multithreading:** Utilizes multithreading for efficient task execution and resource utilization on both Master and Worker nodes.
- **TCP Communication:** Implements TCP sockets for reliable and efficient communication between Master and Workers.

### Frontend
- **Android Application:** Connects to the Master via TCP sockets for all operations.
- **Asynchronous Operations:** Use of threads to keep the UI responsive during data processing.
- **Accommodation Search:** Start by searching for available accommodations based on your preferences such as location, dates, number of persons, price range, and rating.
- **Booking and Review:** Once you find a suitable room, proceed to book it. After booking, you'll have the opportunity to leave a review based on your experience.

## Contributors
- Pantelidis Ippokratis [[Ippo03]](https://github.com/Ippo03)
- Mitsakis Nikolaos [[NIKOMAHOS]](https://github.com/NIKOMAHOS)
- Augerakis Vasilis [[Augeraki]](https://github.com/Augeraki)


<br>

You can find a demo video here :

<div style="text-align:center;">
  <video width="320" height="560" controls>
    <source src="https://drive.google.com/uc?export=download&id=1FKU-73MLF4oETGqRO-2OMYfjkCcTG84d" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</div>


