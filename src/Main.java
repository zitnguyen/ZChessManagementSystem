

import database.DatabaseInit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting database initialization...");

        try {
            DatabaseInit.initDatabase();
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Done!");
    }
}
