import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyManager {

    // Method to Create (Insert) a new Faculty member
    public static void addFaculty(String id, String name, int maxWorkload) {
        String query = "INSERT INTO Faculty (Faculty_ID, Name, Max_Workload) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, maxWorkload);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Faculty member added successfully!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding faculty: " + e.getMessage());
        }
    }

    // Method to Read (Select) all Faculty members
    public static void viewAllFaculty() {
        String query = "SELECT * FROM Faculty";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\n--- Faculty List ---");
            System.out.printf("%-15s | %-25s | %-15s\n", "Faculty ID", "Name", "Max Workload");
            System.out.println("---------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-15s | %-25s | %-15s\n", 
                    rs.getString("Faculty_ID"), 
                    rs.getString("Name"), 
                    rs.getInt("Max_Workload"));
            }
            System.out.println("---------------------------------------------------------------\n");
            
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving faculty: " + e.getMessage());
        }
    }

    // Method to check total workload (Aggregate & JOIN operation)
    public static void checkWorkload(String facultyId) {
        String query = "SELECT f.Name, f.Max_Workload, COALESCE(SUM(c.Credits), 0) AS Current_Workload " +
                       "FROM Faculty f " +
                       "LEFT JOIN Timetable t ON f.Faculty_ID = t.Faculty_ID " +
                       "LEFT JOIN Courses c ON t.Course_Code = c.Course_Code " +
                       "WHERE f.Faculty_ID = ? " +
                       "GROUP BY f.Faculty_ID, f.Name, f.Max_Workload";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, facultyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("Name");
                int max = rs.getInt("Max_Workload");
                int current = rs.getInt("Current_Workload");
                
                System.out.println("\n--- Workload Report ---");
                System.out.println("Faculty Name: " + name);
                System.out.println("Current Workload: " + current + " credits");
                System.out.println("Max Allowed: " + max + " credits");
                
                if (current > max) {
                    System.out.println("🚨 WARNING: Faculty is OVERLOADED!");
                } else {
                    System.out.println("✅ Workload is within limits.");
                }
                System.out.println("-----------------------\n");
            } else {
                System.out.println("❌ Faculty ID not found.");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculating workload: " + e.getMessage());
        }
    }
}