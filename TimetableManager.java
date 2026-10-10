import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TimetableManager {

    // Method to allocate a class and test for clashes
    public static void allocateClass(String scheduleId, String facultyId, String courseCode, String roomNo, String timeSlot) {
        String query = "INSERT INTO Timetable (Schedule_ID, Faculty_ID, Course_Code, Room_No, Time_Slot) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, scheduleId);
            pstmt.setString(2, facultyId);
            pstmt.setString(3, courseCode);
            pstmt.setString(4, roomNo);
            pstmt.setString(5, timeSlot);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Class scheduled successfully! No clashes detected.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("🚨 CLASH DETECTED: The chosen Faculty or Room is already booked for this Time Slot!");
            } else {
                System.err.println("❌ Error scheduling class: " + e.getMessage());
            }
        }
    }

    // Method to view the master schedule (JOIN operation)
    public static void viewTimetable() {
        String query = "SELECT t.Schedule_ID, f.Name AS Faculty_Name, c.Course_Title, t.Room_No, t.Time_Slot " +
                       "FROM Timetable t " +
                       "JOIN Faculty f ON t.Faculty_ID = f.Faculty_ID " +
                       "JOIN Courses c ON t.Course_Code = c.Course_Code " +
                       "ORDER BY t.Time_Slot";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\n--- Master Timetable ---");
            System.out.printf("%-12s | %-20s | %-25s | %-10s | %-15s\n", "Schedule ID", "Faculty Name", "Course Title", "Room No", "Time Slot");
            System.out.println("---------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-12s | %-20s | %-25s | %-10s | %-15s\n", 
                    rs.getString("Schedule_ID"), 
                    rs.getString("Faculty_Name"), 
                    rs.getString("Course_Title"),
                    rs.getString("Room_No"),
                    rs.getString("Time_Slot"));
            }
            System.out.println("---------------------------------------------------------------------------------------------\n");
            
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving timetable: " + e.getMessage());
        }
    }
// Method to Update (Substitute) a faculty member for an existing class
    public static void substituteFaculty(String scheduleId, String newFacultyId) {
        String query = "UPDATE Timetable SET Faculty_ID = ? WHERE Schedule_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, newFacultyId);
            pstmt.setString(2, scheduleId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Substitution successful! No schedule conflicts for the new faculty.");
            } else {
                System.out.println("❌ Schedule ID not found.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("🚨 CLASH DETECTED: The substitute faculty is already teaching a class during this Time Slot!");
            } else {
                System.err.println("❌ Error updating substitution: " + e.getMessage());
            }
        }
    }

    // Method to Delete (Cancel) a scheduled class
    public static void cancelClass(String scheduleId) {
        String query = "DELETE FROM Timetable WHERE Schedule_ID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, scheduleId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Class cancelled and removed from the timetable.");
            } else {
                System.out.println("❌ Schedule ID not found.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error cancelling class: " + e.getMessage());
        }
    }
}