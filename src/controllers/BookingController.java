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

        // Проверка 1: Существует ли фильм?
        if (!movieRepo.existsById(movieId)) {
            System.out.println("❌ Movie not found!");
            return;
        }

        System.out.print("Seat ID: ");
        int seatId = scanner.nextInt();

        // Проверка 2: Существует ли такое место?
        if (!bookingRepo.doesSeatExist(seatId)) {
            System.out.println("❌ Seat not found!");
            return;
        }

        // Проверка 3: Не занято ли оно?
        if (bookingRepo.isSeatTaken(seatId, movieId)) {
            System.out.println("❌ Seat already booked!");
            return;
        }

        bookingRepo.createBooking(1, movieId, seatId, 1000);
        System.out.println("✅ Ticket booked successfully!");
    }
}