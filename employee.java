import java.sql.*;
import java.util.Scanner;

public class EmployeeDatabaseApp {
    static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    static final String USER = "root";
    static final String PASS = "1234";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to database!");

            while (true) {
                System.out.println("\n1.Add 2.View 3.Update 4.Delete 5.Exit");
                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 1 -> addEmployee(sc);
                    case 2 -> viewEmployees();
                    case 3 -> updateEmployee(sc);
                    case 4 -> deleteEmployee(sc);
                    case 5 -> {
                        conn.close();
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter position: ");
        String position = sc.nextLine();
        System.out.print("Enter salary: ");
        double salary = sc.nextDouble();

        String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setString(2, position);
        pst.setDouble(3, salary);

        int rows = pst.executeUpdate();
        System.out.println(rows + " employee(s) added.");
    }

    static void viewEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            System.out.printf("ID: %d, Name: %s, Position: %s, Salary: %.2f%n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getString("position"), rs.getDouble("salary"));
        }
    }

    static void updateEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("New name: ");
        String name = sc.nextLine();
        System.out.print("New position: ");
        String position = sc.nextLine();
        System.out.print("New salary: ");
        double salary = sc.nextDouble();

        String sql = "UPDATE employees SET name=?, position=?, salary=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setString(2, position);
        pst.setDouble(3, salary);
        pst.setInt(4, id);

        int rows = pst.executeUpdate();
        System.out.println(rows + " employee(s) updated.");
    }

    static void deleteEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM employees WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);

        int rows = pst.executeUpdate();
        System.out.println(rows + " employee(s) deleted.");
    }
}
