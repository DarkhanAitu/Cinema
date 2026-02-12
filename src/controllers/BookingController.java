package controllers;

import models.Movie;
import models.MovieCategory;
import models.User;
import repositories.BookingRepository;
import repositories.MovieRepository;

import java.util.List;
import java.util.Scanner;

public class BookingController {

    private final BookingRepository bookingRepo = new BookingRepository();
    private final MovieRepository movieRepo = new MovieRepository();
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public BookingController(User currentUser) {
        this.currentUser = currentUser;
    }

    public void showMovies() {
        System.out.println("""
            Choose category:
            1. All movies
            2. HORROR
            3. SCI_FI
            4. COMEDY
            5. ACTION
            6. DRAMA
            7. ROMANCE
            """);

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }

        List<Movie> movies;

        switch (choice) {
            case 1 -> movies = movieRepo.getAll();
            case 2 -> movies = movieRepo.getByCategory(MovieCategory.HORROR);
            case 3 -> movies = movieRepo.getByCategory(MovieCategory.SCI_FI);
            case 4 -> movies = movieRepo.getByCategory(MovieCategory.COMEDY);
            case 5 -> movies = movieRepo.getByCategory(MovieCategory.ACTION);
            case 6 -> movies = movieRepo.getByCategory(MovieCategory.DRAMA);
            case 7 -> movies = movieRepo.getByCategory(MovieCategory.ROMANCE);
            default -> {
                System.out.println("Invalid category!");
                return;
            }
        }

        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        System.out.println("\nAvailable movies:");
        movies.forEach(m -> System.out.println(
                m.getId() + " | " +
                        m.getTitle() + " (" + m.getDuration() + " min) - $" +
                        m.getPrice() +
                        " | Category: " + m.getCategory()
        ));
    }

    public void bookTicket() {
        System.out.print("Enter Movie ID to book: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.showSeatsWithStatus(movieId);

        System.out.print("Enter Seat ID: ");
        int seatId = Integer.parseInt(scanner.nextLine());

        if (!bookingRepo.doesSeatExist(seatId)) {
            System.out.println("Seat does not exist!");
            return;
        }

        if (bookingRepo.isSeatTaken(seatId, movieId)) {
            System.out.println("Seat already booked!");
            return;
        }

        double basePrice = movieRepo.getPrice(movieId);
        double seatPrice = bookingRepo.getSeatPrice(seatId);
        double finalPrice = basePrice + seatPrice;

        bookingRepo.createBooking(
                currentUser.getId(),
                movieId,
                seatId,
                finalPrice
        );

        System.out.println("Ticket booked successfully! Price: $" + finalPrice);
    }


    public void showMyBookings() {
        System.out.print("Enter Movie ID to view booking details: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.getFullBooking(currentUser.getId(), movieId);
    }
    public void cancelBooking() {
        System.out.print("Enter Movie ID of the booking you want to cancel: ");
        int movieId;
        try {
            movieId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Movie ID!");
            return;
        }

        System.out.println("Your bookings for this movie:");
        bookingRepo.getFullBooking(currentUser.getId(), movieId);

        System.out.print("Enter Booking ID you want to cancel: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Booking ID!");
            return;
        }

        boolean success = bookingRepo.cancelBooking(bookingId, currentUser.getId());
        if (success) {
            System.out.println("Booking canceled successfully.");
        } else {
            System.out.println("Failed to cancel booking. Make sure the Booking ID is correct.");
        }
    }

}
