package ui;

import model.User;

import java.util.Scanner;

/** Screen shown after a successful login. Logging out returns to the main menu. */
public class DashboardMenu {

    private final Scanner scanner;
    private final User user;

    public DashboardMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        this.user = user;
    }

    public void show() {
        System.out.println("\nWelcome, " + user.username() + "!");
        System.out.println("Role  : " + user.roles());
        System.out.println("Status: " + user.status());
        if ("PENDING".equals(user.status())) {
            System.out.println("(Your account is still waiting for admin approval.)");
        }

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- DASHBOARD ---");
            System.out.println("1. Logout");
            System.out.print("Choice: ");
            if (scanner.nextLine().trim().equals("1")) {
                System.out.println("You have been logged out.");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
