package controllers;

import controllers.interfaces.IAdminController;
import models.Movie;
import models.MovieCategory;
import models.User;
import Factories.MovieFactory;
import repositories.BookingRepository;
import repositories.MovieRepository;
import repositories.UserRepository;

import java.util.Scanner;
import java.util.List;

public class AdminController implements IAdminController {

    private final Scanner scanner = new Scanner(System.in);
    private final MovieRepository movieRepo = new MovieRepository();
    private final UserRepository userRepo = new UserRepository();
    private final User currentUser;
    private final BookingRepository bookingRepo = new BookingRepository();

    public AdminController(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void addMovie() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            System.out.println("Access denied.");
            return;
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Category (ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCI_FI): ");
        MovieCategory category;
        try {
            category = MovieCategory.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            category = MovieCategory.OTHER;
        }

        Movie movie = MovieFactory.createMovie(0, title, duration, price, category);
        movieRepo.addMovie(movie);

        System.out.println("Movie added successfully.");
    }

    @Override
    public void addAdmin() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            System.out.println("Access denied.");
            return;
        }

        System.out.print("New admin username: ");
        String username = scanner.nextLine().trim();

        if (userRepo.findByUsername(username) != null) {
            System.out.println("User already exists.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("admin");

        userRepo.addUser(admin);
        System.out.println("New admin added.");
    }
    public void showFullBookingForMovie() {
        System.out.print("Enter Movie ID to view all bookings: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.getFullBookingByMovie(movieId);
    }
    public void editMovie() {
        System.out.print("Enter Movie ID to edit: ");
        int movieId;
        try {
            movieId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Movie ID!");
            return;
        }

        List<Movie> allMovies = movieRepo.getAll();
        Movie movie = allMovies.stream().filter(m -> m.getId() == movieId).findFirst().orElse(null);
        if (movie == null) {
            System.out.println("Movie not found!");
            return;
        }

        System.out.println("Editing movie: " + movie.getTitle());

        System.out.println("What do you want to edit?");
        System.out.println("1. Title");
        System.out.println("2. Duration");
        System.out.println("3. Price");
        System.out.println("4. Category");
        System.out.println("5. Cancel");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }

        String newTitle = null;
        Integer newDuration = null;
        Double newPrice = null;
        MovieCategory newCategory = null;

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new title: ");
                newTitle = scanner.nextLine().trim();
            }
            case 2 -> {
                System.out.print("Enter new duration (minutes): ");
                newDuration = Integer.parseInt(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Enter new price: ");
                newPrice = Double.parseDouble(scanner.nextLine());
            }
            case 4 -> {
                System.out.print("Enter new category (ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCI_FI, OTHER): ");
                try {
                    newCategory = MovieCategory.valueOf(scanner.nextLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid category, keeping current.");
                }
            }
            case 5 -> {
                System.out.println("Edit canceled.");
                return;
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        boolean updated = movieRepo.updateMovie(movieId, newTitle, newDuration, newPrice, newCategory);
        if (updated) {
            System.out.println("Movie updated successfully!");
        } else {
            System.out.println("Failed to update movie.");
        }
    }


}
