package controllers;

import controllers.interfaces.ILoginController;
import models.User;
import repositories.UserRepository;

import java.util.Scanner;

public class LoginController implements ILoginController {

    private final Scanner scanner = new Scanner(System.in);
    private final UserRepository userRepo = new UserRepository();
    private User currentUser;

    @Override
    public boolean login() {

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
                return false;
            }

            currentUser = user;
            System.out.println("Logged in as: " + user.getUsername() + " (ADMIN)");
            return true;
        }

        if (!role.equals("customer")) {
            System.out.println("Invalid role!");
            return false;
        }

        System.out.println("""
        1. Login
        2. Sign up
        """);

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return false;
        }

        if (choice == 1) {

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            User user = userRepo.findByUsername(username);

            if (user == null
                    || !user.getRole().equalsIgnoreCase("customer")
                    || !password.equals(user.getPassword())) {

                System.out.println("Invalid username or password.");
                return false;
            }

            currentUser = user;
            System.out.println("Logged in as: " + user.getUsername() + " (CUSTOMER)");
            return true;
        }

        if (choice == 2) {

            System.out.print("Choose username: ");
            String username = scanner.nextLine().trim();

            if (userRepo.findByUsername(username) != null) {
                System.out.println("Username already exists.");
                return false;
            }

            System.out.print("Choose password: ");
            String password = scanner.nextLine().trim();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("customer");

            userRepo.addUser(user);

            currentUser = userRepo.findByUsername(username);
            System.out.println("Signup successful! Logged in as: " + username + " (CUSTOMER)");
            return true;
        }

        System.out.println("Invalid choice!");
        return false;
    }


    @Override
    public String getCurrentUserRole() {
        return currentUser == null ? "" : currentUser.getRole();
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void logout() {
        if (currentUser != null) {
            System.out.println("User " + currentUser.getUsername() + " logged out successfully.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

}
