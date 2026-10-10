import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Update these credentials to match your local MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/FacultyTimetableDB";
    private static final String USER = "root";
    private static final String PASSWORD = "zarina@mysql_3322"; // Insert your root password here

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC Driver (Optional in newer JDBC versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure the connector JAR is in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed. Check your URL, username, and password.");
            e.printStackTrace();
        }
        return connection;
    }
}