import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("==================================================");
        System.out.println("  Faculty Workload & Timetable Management System  ");
        System.out.println("==================================================");

        // Test the database connection on startup
        DBConnection.getConnection();

        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Add New Faculty");
            System.out.println("2. View All Faculty");
            System.out.println("3. Add New Course");
            System.out.println("4. View All Courses");
            System.out.println("5. Schedule a Class (Create)");
            System.out.println("6. View Master Timetable (Read & JOIN)");
            System.out.println("7. Check Faculty Workload (Aggregate)");
            System.out.println("8. Substitute Faculty (Update)");
            System.out.println("9. Cancel a Class (Delete)");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Faculty ID (e.g., FAC001): ");
                    String fId = scanner.nextLine();
                    System.out.print("Enter Faculty Name: ");
                    String fName = scanner.nextLine();
                    System.out.print("Enter Max Workload (hours): ");
                    try {
                        int workload = Integer.parseInt(scanner.nextLine());
                        FacultyManager.addFaculty(fId, fName, workload);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Workload must be a number.");
                    }
                    break;
                case "2":
                    FacultyManager.viewAllFaculty();
                    break;
                case "3":
                    System.out.print("Enter Course Code (e.g., DS101): ");
                    String cCode = scanner.nextLine();
                    System.out.print("Enter Course Title: ");
                    String cTitle = scanner.nextLine();
                    System.out.print("Enter Credits: ");
                    try {
                        int credits = Integer.parseInt(scanner.nextLine());
                        CourseManager.addCourse(cCode, cTitle, credits);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Credits must be a number.");
                    }
                    break;
                case "4":
                    CourseManager.viewAllCourses();
                    break;
                case "5":
                    System.out.print("Enter Schedule ID (e.g., SCH001): ");
                    String sId = scanner.nextLine();
                    System.out.print("Enter Faculty ID: ");
                    String facultyId = scanner.nextLine();
                    System.out.print("Enter Course Code: ");
                    String courseCode = scanner.nextLine();
                    System.out.print("Enter Room No (e.g., R-101): ");
                    String roomNo = scanner.nextLine();
                    System.out.print("Enter Time Slot (e.g., Mon 9:00 AM): ");
                    String timeSlot = scanner.nextLine();
                    TimetableManager.allocateClass(sId, facultyId, courseCode, roomNo, timeSlot);
                    break;
                case "6":
                    TimetableManager.viewTimetable();
                    break;
                case "7":
                    System.out.print("Enter Faculty ID to check workload: ");
                    String checkId = scanner.nextLine();
                    FacultyManager.checkWorkload(checkId);
                    break;
                case "8":
                    System.out.print("Enter Schedule ID to modify: ");
                    String subScheduleId = scanner.nextLine();
                    System.out.print("Enter New Faculty ID for substitution: ");
                    String newFacId = scanner.nextLine();
                    TimetableManager.substituteFaculty(subScheduleId, newFacId);
                    break;
                case "9":
                    System.out.print("Enter Schedule ID to cancel: ");
                    String cancelId = scanner.nextLine();
                    TimetableManager.cancelClass(cancelId);
                    break;
                case "10":
                    exit = true;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}