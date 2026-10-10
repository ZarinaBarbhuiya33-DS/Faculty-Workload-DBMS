import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseManager {

    public static void addCourse(String code, String title, int credits) {
        String query = "INSERT INTO Courses (Course_Code, Course_Title, Credits) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, code);
            pstmt.setString(2, title);
            pstmt.setInt(3, credits);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Course added successfully!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding course: " + e.getMessage());
        }
    }

    public static void viewAllCourses() {
        String query = "SELECT * FROM Courses";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\n--- Course List ---");
            System.out.printf("%-15s | %-35s | %-10s\n", "Course Code", "Course Title", "Credits");
            System.out.println("-------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-15s | %-35s | %-10s\n", 
                    rs.getString("Course_Code"), 
                    rs.getString("Course_Title"), 
                    rs.getInt("Credits"));
            }
            System.out.println("-------------------------------------------------------------------\n");
            
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving courses: " + e.getMessage());
        }
    }
}