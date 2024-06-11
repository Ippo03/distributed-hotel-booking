package org.backend.misc;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class CommandInput {
    public static DateRange inputDateRange(){
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date startDate;
        Date endDate;

        while (true) {
            System.out.print("Enter start date (dd-MM-yyyy): ");
            String startDateStr = scanner.nextLine();

            System.out.print("Enter end date (dd-MM-yyyy): ");
            String endDateStr = scanner.nextLine();

            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);

                if (startDate.after(endDate)) {
                    System.out.println("Start date cannot be after end date. Please try again.");
                } else {
                    break; // Exit the loop if the input is valid
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter dates in the format dd-MM-yyyy.");
            }
        }

        return new DateRange(startDate, endDate);
    }
}
