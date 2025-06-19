package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:sqlite:C:/Users/ПК/Desktop/DataBase/Lab10.db";

    public static void main(String[] args) {
        try {

            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL);
                 Scanner scanner = new Scanner(System.in)) {

                System.out.println("Connected to the database.");

                while (true) {
                    System.out.println("\nSelect an action:");
                    System.out.println("1 - Show all records");
                    System.out.println("2 - Add a record");
                    System.out.println("3 - Update email by ID");
                    System.out.println("4 - Delete a record by ID");
                    System.out.println("0 - Exit");

                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            readAll(conn);
                            break;
                        case 2:
                            System.out.print("Enter name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter email: ");
                            String email = scanner.nextLine();
                            insertUser(conn, name, email);
                            break;
                        case 3:
                            System.out.print("Enter ID to update: ");
                            int updateId = Integer.parseInt(scanner.nextLine());
                            System.out.print("Enter new email: ");
                            String newEmail = scanner.nextLine();
                            updateEmail(conn, updateId, newEmail);
                            break;
                        case 4:
                            System.out.print("Enter ID to delete: ");
                            int deleteId = Integer.parseInt(scanner.nextLine());
                            deleteUser(conn, deleteId);
                            break;
                        case 0:
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice");
                    }
                }

            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found. Please add the driver to the classpath.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void readAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("ID | Name | Email");
            while (rs.next()) {
                System.out.printf("%d | %s | %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        }
    }

    private static void insertUser(Connection conn, String name, String email) throws SQLException {
        String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Record added.");
        }
    }

    private static void updateEmail(Connection conn, int id, String newEmail) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, id);
            int count = pstmt.executeUpdate();
            if (count > 0) System.out.println("Email updated.");
            else System.out.println("User with this ID not found.");
        }
    }

    private static void deleteUser(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int count = pstmt.executeUpdate();
            if (count > 0) System.out.println("User deleted.");
            else System.out.println("User with this ID not found.");
        }
    }
}
