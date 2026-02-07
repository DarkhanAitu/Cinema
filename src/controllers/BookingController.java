package controllers;

import models.Movie;
import models.MovieCategory;
import models.User;
import Factories.MovieFactory;
import repositories.BookingRepository;
import repositories.MovieRepository;
import repositories.UserRepository;

import java.util.List;
import java.util.Scanner;

public class BookingController {

    private final BookingRepository bookingRepo = new BookingRepository();
    private final MovieRepository movieRepo = new MovieRepository();
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;
    private final UserRepository userRepo = new UserRepository();


    public void login() {
        System.out.print("Are you an admin or a customer? (admin/customer): ");
        String role = scanner.nextLine().trim().toLowerCase();

        if (role.equals("admin")) {
            System.out.print("Enter admin username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter admin password: ");
            String password = scanner.nextLine().trim();

            User user = userRepo.findByUsername(username);

            if (user == null
                    || !user.getRole().equalsIgnoreCase("admin")
                    || !user.getPassword().equals(password)) {
                System.out.println("Invalid username or password. Access denied.");
                return;
            }


            currentUser = user;
            System.out.println("Logged in as: " + currentUser.getUsername() + " (ADMIN)");

        } else {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            User user = userRepo.findByUsername(name);

            if (user == null) {
                User newUser = new User();
                newUser.setUsername(name);
                newUser.setRole("customer");
                userRepo.addUser(newUser);

                user = userRepo.findByUsername(name);
            }

            currentUser = user;
            System.out.println("Logged in as: " + currentUser.getUsername() + " (CUSTOMER)");
        }
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

        System.out.println("Available movies:");

        movies.forEach(m -> System.out.println(
                m.getId() + " | " +
                        m.getTitle() + " (" + m.getDuration() + " min) - $" +
                        m.getPrice() +
                        " | Category: " + m.getCategory()
        ));
    }


    public void addMovie() {
        if (currentUser == null || !currentUser.getRole().equals("admin")) {
            System.out.println("Access denied. Only admins can add movies.");
            return;
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Category (ACTION, COMEDY, DRAMA, HORROR, ROMANCE ,SCI_FI): ");
        String categoryInput = scanner.nextLine().toUpperCase();

        MovieCategory category;
        try {
            category = MovieCategory.valueOf(categoryInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category! Defaulting to ACTION.");
            category = MovieCategory.ACTION;
        }

        Movie movie = MovieFactory.createMovie(0, title, duration, price, category);
        movieRepo.addMovie(movie);

        System.out.println("Movie added successfully! Category: " + movie.getCategory());
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

        double basePrice = movieRepo.getPrice(movieId); // цена фильма

        double seatPrice = bookingRepo.getSeatPrice(seatId); // цена места: 150, 300, 500

        double finalPrice = basePrice + seatPrice;

        bookingRepo.createBooking(currentUser.getId(), movieId, seatId, finalPrice);

        System.out.println("Ticket booked successfully! Price: $" + finalPrice);

    }

    public void addAdmin() {
        if (currentUser == null || !currentUser.getRole().equalsIgnoreCase("admin")) {
            System.out.println("Access denied. Only admins can add new admins.");
            return;
        }

        System.out.print("Enter new admin's username: ");
        String username = scanner.nextLine().trim();

        if (userRepo.findByUsername(username) != null) {
            System.out.println("User with this username already exists!");
            return;
        }

        System.out.print("Enter new admin's password: ");
        String password = scanner.nextLine().trim();

        User newAdmin = new User();
        newAdmin.setUsername(username);
        newAdmin.setRole("admin");
        newAdmin.setPassword(password);  // Set the password for the new admin

        if (userRepo.addUser(newAdmin)) {
            System.out.println("New admin added successfully: " + username);
        } else {
            System.out.println("Failed to add new admin. Try again.");
        }
    }

    public void showFullBooking() {
        System.out.print("Enter Movie ID to view all bookings: ");
        int movieId = Integer.parseInt(scanner.nextLine());
        bookingRepo.getFullBooking(currentUser.getId(), movieId);
    }
}