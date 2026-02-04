package controllers;

import models.Movie;
import models.User;
import repositories.BookingRepository;
import repositories.MovieRepository;
import Factories.MovieFactory;
import models.MovieCategory;
import repositories.CategoryRepository;


import java.util.List;
import java.util.Scanner;

public class BookingController {

    private final BookingRepository bookingRepo = new BookingRepository();
    private final MovieRepository movieRepo = new MovieRepository();
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;

    public void login() {
        String roleInput;

        while (true) {
            System.out.print("Are you an admin or a customer? (1 = admin, 2 = customer): ");
            roleInput = scanner.nextLine().trim();

            if (roleInput.equals("1") || roleInput.equals("2")) {
                break;
            }

            System.out.println("Invalid choice. Please enter 1 or 2.");
        }

        String username;
        if (roleInput.equals("1")) {
            username = "admin_user";
        } else {
            System.out.print("Enter your name: ");
            username = scanner.nextLine().trim();

            if (username.isBlank()) {
                username = "guest";
            }
        }

        currentUser = MovieFactory.createUser(roleInput, username);

        System.out.println(
                "Logged in as: " +
                        currentUser.getUsername() +
                        " (" + currentUser.getRole() + ")"
        );
    }


    public String getCurrentUserRole() {
        if (currentUser == null) return "";
        return currentUser.getRole();
    }

    public void showMovies() {
        List<Movie> movies = movieRepo.getAll();
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        for (Movie m : movies) {
            System.out.println(m);
        }
    }

    public void addMovie() {
        if (!currentUser.getRole().equals("admin")) {
            System.out.println("You are not allowed to add movies.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        CategoryRepository categoryRepo = new CategoryRepository();

        System.out.print("Movie title: ");
        String title = scanner.nextLine();
        if (title == null || title.isBlank()) {
            System.out.println("Title cannot be empty");
            return;
        }

        List<MovieCategory> categories = categoryRepo.getAll();
        if (categories.isEmpty()) {
            System.out.println("No categories available! Add categories first.");
            return;
        }

        System.out.println("Available categories:");
        categories.forEach(c -> System.out.println(c.getId() + " | " + c.getName()));


        MovieCategory category = null;
        while (category == null) {
            System.out.print("Choose category ID: ");
            int catId;
            try {
                catId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Category ID must be a number!");
                continue;
            }

            category = categoryRepo.getById(catId);
            if (category == null) {
                System.out.println("Invalid category! Please choose a valid ID.");
            }
        }


        int duration = 0;
        while (duration <= 0) {
            System.out.print("Duration (min): ");
            try {
                duration = Integer.parseInt(scanner.nextLine());
                if (duration <= 0) {
                    System.out.println("Duration must be positive!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Duration must be a number!");
            }
        }

        double price = -1;
        while (price < 0) {
            System.out.print("Price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("Price cannot be negative!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Price must be a number!");
            }
        }


        Movie movie = new Movie(0, title, duration, price, category);
        movieRepo.addMovie(movie);

        System.out.println("Movie added successfully! Category: " + category.getName());
    }


    public void bookTicket() {
        System.out.print("Movie ID: ");
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

        double price = bookingRepo.getSeatPrice(seatId);
        bookingRepo.createBooking(currentUser.getId(), movieId, seatId, price);

        System.out.println("Ticket booked successfully! Price: " + price);
    }

    public void showFullBooking() {
        System.out.print("Enter Movie ID to view all bookings: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.getFullBooking(currentUser.getId(), movieId);
    }
}
