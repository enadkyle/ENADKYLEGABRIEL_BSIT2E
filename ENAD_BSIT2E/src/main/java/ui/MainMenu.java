package ui;

import dao.UserDao;
import model.User;

import java.util.Optional;
import java.util.Scanner;

/** First screen: Register, Login, Exit. */
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=====================================");
            System.out.println("        TOWGO TOWING SERVICE");
            System.out.println("=====================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "0" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- REGISTER ---");
        String username = askRequired("Username: ");
        if (userDao.usernameExists(username)) {
            System.out.println("That username is already taken.");
            return;
        }
        String password = askRequired("Password: ");

        System.out.println("Select role:");
        System.out.println("1. STAFF");
        System.out.println("2. DRIVER");
        System.out.print("Choice: ");
        String role;
        switch (scanner.nextLine().trim()) {
            case "1" -> role = "STAFF";
            case "2" -> role = "DRIVER";
            default -> {
                System.out.println("Invalid role. Registration cancelled.");
                return;
            }
        }

        if (userDao.register(username, password, role)) {
            System.out.println("Account created as " + role + ". Status: PENDING (waiting for admin approval).");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private void login() {
        System.out.println("\n--- LOGIN ---");
        String username = askRequired("Username: ");
        String password = askRequired("Password: ");

        Optional<User> result = userDao.login(username, password);
        if (result.isPresent()) {
            new DashboardMenu(scanner, result.get()).show();   // returns here after logout
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private String askRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("This field is required.");
        }
    }
}
