package controllers;

import controllers.interfaces.IBookingController;
import repositories.*;
import java.util.Scanner;

public class BookingController implements IBookingController {

    private final MovieRepository movieRepo = new MovieRepository();
    private final BookingRepository bookingRepo = new BookingRepository();


    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void showMovies() {
        movieRepo.getAll().forEach(System.out::println);
    }

    @Override
    public void bookTicket() {



        System.out.print("Movie ID: ");
        int movieId = scanner.nextInt();

        System.out.print("Seat ID: ");
        int seatId = scanner.nextInt();

        if (bookingRepo.isSeatTaken(seatId, movieId)) {
            System.out.println("❌ Seat already booked!");
            return;
        }

        bookingRepo.createBooking(1, movieId, seatId, 1000);
        System.out.println("✅ Ticket booked successfully!");
    }
}
