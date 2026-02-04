import controllers.BookingController;

import java.util.Scanner;

public class MyApplication {
    public static void main(String[] args) {
        BookingController controller = new BookingController();
        Scanner scanner = new Scanner(System.in);

        controller.login();

        while (true) {
            System.out.println("1. Show movies");
            System.out.println("2. Book ticket");
            System.out.println("3. Show full booking info");

            if (controller.getCurrentUserRole().equals("admin")) {
                System.out.println("4. Add new movie");
                System.out.println("5. Exit");
            } else {
                System.out.println("4. Exit");
            }

            int choice = Integer.parseInt(scanner.nextLine());

            if (controller.getCurrentUserRole().equals("admin")) {
                switch (choice) {
                    case 1 -> controller.showMovies();
                    case 2 -> controller.bookTicket();
                    case 3 -> controller.showFullBooking();
                    case 4 -> controller.addMovie();
                    case 5 -> {
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice");
                }
            } else {
                switch (choice) {
                    case 1 -> controller.showMovies();
                    case 2 -> controller.bookTicket();
                    case 3 -> controller.showFullBooking();
                    case 4 -> {
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice");
                }
            }
        }
    }
}