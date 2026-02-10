import controllers.AdminController;
import controllers.BookingController;
import controllers.LoginController;
import models.User;

import java.util.Scanner;

public class MyApplication {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        LoginController loginController = new LoginController();

        while (true) {
            while (!loginController.isLoggedIn()) {
                System.out.println("\n--- LOGIN ---");
                if (!loginController.login()) {
                    System.out.println("Please try again.\n");
                }
            }

            User currentUser = loginController.getCurrentUser();

            BookingController bookingController = new BookingController(currentUser);
            AdminController adminController = new AdminController(currentUser);

            boolean sessionActive = true;
            while (sessionActive) {
                System.out.println("\n--- MENU ---");

                System.out.println("1. Show movies");

                if (loginController.getCurrentUserRole().equalsIgnoreCase("admin")) {
                    System.out.println("2. Show full booking info");
                    System.out.println("3. Add new movie");
                    System.out.println("4. Edit movie");
                    System.out.println("5. Add new admin");
                    System.out.println("6. Logout");
                    System.out.println("7. Exit");
                } else {
                    System.out.println("2. Show my bookings");
                    System.out.println("3. Book ticket");
                    System.out.println("4. Cancel booking");
                    System.out.println("5. Logout");
                    System.out.println("6. Exit");
                }

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input!");
                    continue;
                }

                if (loginController.getCurrentUserRole().equalsIgnoreCase("admin")) {
                    switch (choice) {
                        case 1 -> bookingController.showMovies();
                        case 2 -> adminController.showFullBookingForMovie();
                        case 3 -> adminController.addMovie();
                        case 4 -> adminController.editMovie();
                        case 5 -> adminController.addAdmin();
                        case 6 -> {
                            loginController.logout();
                            sessionActive = false;
                        }
                        case 7 -> System.exit(0);
                        default -> System.out.println("Invalid choice");
                    }
                } else { // customer
                    switch (choice) {
                        case 1 -> bookingController.showMovies();
                        case 2 -> bookingController.showMyBookings();
                        case 3 -> bookingController.bookTicket();
                        case 4 -> bookingController.cancelBooking();
                        case 5 -> {
                            loginController.logout();
                            sessionActive = false;
                        }
                        case 6 -> System.exit(0);
                        default -> System.out.println("Invalid choice");
                    }
                }
            }
        }
    }

}
