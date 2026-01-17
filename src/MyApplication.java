import controllers.BookingController;
import java.util.Scanner;

public class MyApplication {

    private final BookingController controller = new BookingController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("""
                1. Show movies
                2. Book ticket
                3. Exit
                """);

            int choice = scanner.nextInt();

            if (choice == 1) {
                controller.showMovies();
            } else if (choice == 2) {
                controller.bookTicket();
            } else {
                break;
            }
        }
    }
}
