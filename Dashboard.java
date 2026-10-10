import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.io.PrintStream;

public class Dashboard {
    
    // Modern Color Palette
    private static final Color DARK_BG = new Color(30, 34, 40);
    private static final Color PANEL_BG = new Color(43, 48, 58);
    private static final Color ACCENT_BLUE = new Color(64, 115, 255);
    private static final Color ACCENT_RED = new Color(231, 76, 60);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color TEXT_LIGHT = new Color(230, 230, 230);

    public static void main(String[] args) {
        // Set cross-platform look and feel for consistency
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main Window Setup
        JFrame frame = new JFrame("DBMS Prototype - Woxsen University");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(DARK_BG);

        // Top Header
        JLabel headerLabel = new JLabel("🎓 Faculty Workload & Timetable Intelligence System", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(ACCENT_BLUE);
        frame.add(headerLabel, BorderLayout.NORTH);

        // Center Screen (Console Monitor)
        JTextArea console = new JTextArea();
        console.setEditable(false);
        console.setFont(new Font("Consolas", Font.PLAIN, 15));
        console.setBackground(new Color(20, 20, 25));
        console.setForeground(ACCENT_GREEN); // Terminal green
        console.setMargin(new Insets(10, 15, 10, 15));
        
        JScrollPane scrollPane = new JScrollPane(console);
        TitledBorder consoleBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PANEL_BG, 2), "Live System Output Monitor");
        consoleBorder.setTitleColor(TEXT_LIGHT);
        consoleBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 15, 15), consoleBorder));
        scrollPane.getViewport().setBackground(new Color(20, 20, 25));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Redirect standard output to the custom console
        PrintStream printStream = new PrintStream(new CustomOutputStream(console));
        System.setOut(printStream);
        System.setErr(printStream);

        // Left Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(10, 1, 10, 10));
        menuPanel.setBorder(new EmptyBorder(0, 15, 15, 10));
        menuPanel.setBackground(DARK_BG);
        menuPanel.setPreferredSize(new Dimension(280, 0));

        // Create interactive buttons
        JButton btnAddFac = createButton("➕ Add New Faculty", PANEL_BG);
        JButton btnViewFac = createButton("👥 View All Faculty", PANEL_BG);
        JButton btnAddCourse = createButton("📚 Add New Course", PANEL_BG);
        JButton btnViewCourses = createButton("📖 View All Courses", PANEL_BG);
        JButton btnSchedule = createButton("📅 Schedule a Class", ACCENT_BLUE);
        JButton btnViewTimetable = createButton("🗓️ Master Timetable", PANEL_BG);
        JButton btnWorkload = createButton("📊 Check Workload", PANEL_BG);
        JButton btnSubstitute = createButton("🔄 Substitute Faculty", new Color(243, 156, 18)); // Warning Orange
        JButton btnCancel = createButton("❌ Cancel a Class", ACCENT_RED);
        JButton btnClear = createButton("🧹 Clear Screen", PANEL_BG);

        // --- Button Actions (Mapping logic to GUI Popups) ---
        btnAddFac.addActionListener(e -> {
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField workloadField = new JTextField();
            Object[] msg = {"Faculty ID (e.g., FAC001):", idField, "Faculty Name:", nameField, "Max Workload (Credits):", workloadField};
            int result = JOptionPane.showConfirmDialog(frame, msg, "Add Faculty", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    FacultyManager.addFaculty(idField.getText(), nameField.getText(), Integer.parseInt(workloadField.getText()));
                } catch (NumberFormatException ex) {
                    System.err.println("❌ Invalid input: Workload must be a numeric value.");
                }
            }
        });

        btnViewFac.addActionListener(e -> FacultyManager.viewAllFaculty());

        btnAddCourse.addActionListener(e -> {
            JTextField codeField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField creditField = new JTextField();
            Object[] msg = {"Course Code (e.g., CS101):", codeField, "Course Title:", titleField, "Credits:", creditField};
            int result = JOptionPane.showConfirmDialog(frame, msg, "Add Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    CourseManager.addCourse(codeField.getText(), titleField.getText(), Integer.parseInt(creditField.getText()));
                } catch (NumberFormatException ex) {
                    System.err.println("❌ Invalid input: Credits must be a numeric value.");
                }
            }
        });

        btnViewCourses.addActionListener(e -> CourseManager.viewAllCourses());

        btnSchedule.addActionListener(e -> {
            JTextField sIdField = new JTextField();
            JTextField fIdField = new JTextField();
            JTextField cCodeField = new JTextField();
            JTextField roomField = new JTextField();
            JTextField timeField = new JTextField();
            Object[] msg = {
                "Schedule ID (e.g., SCH001):", sIdField, 
                "Faculty ID:", fIdField, 
                "Course Code:", cCodeField, 
                "Room No (e.g., Room A):", roomField, 
                "Time Slot (e.g., Mon 9AM):", timeField
            };
            int result = JOptionPane.showConfirmDialog(frame, msg, "Schedule a Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                TimetableManager.allocateClass(sIdField.getText(), fIdField.getText(), cCodeField.getText(), roomField.getText(), timeField.getText());
            }
        });

        btnViewTimetable.addActionListener(e -> TimetableManager.viewTimetable());

        btnWorkload.addActionListener(e -> {
            String facId = JOptionPane.showInputDialog(frame, "Enter Faculty ID to check workload:");
            if (facId != null && !facId.trim().isEmpty()) {
                FacultyManager.checkWorkload(facId);
            }
        });

        btnSubstitute.addActionListener(e -> {
            JTextField sIdField = new JTextField();
            JTextField fIdField = new JTextField();
            Object[] msg = {"Schedule ID to modify:", sIdField, "New Substitute Faculty ID:", fIdField};
            int result = JOptionPane.showConfirmDialog(frame, msg, "Substitute Faculty", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                TimetableManager.substituteFaculty(sIdField.getText(), fIdField.getText());
            }
        });

        btnCancel.addActionListener(e -> {
            String scheduleId = JOptionPane.showInputDialog(frame, "Enter Schedule ID to permanently cancel:");
            if (scheduleId != null && !scheduleId.trim().isEmpty()) {
                TimetableManager.cancelClass(scheduleId);
            }
        });

        btnClear.addActionListener(e -> console.setText(""));

        // Assemble Menu Panel
        menuPanel.add(btnAddFac);
        menuPanel.add(btnViewFac);
        menuPanel.add(btnAddCourse);
        menuPanel.add(btnViewCourses);
        menuPanel.add(btnSchedule);
        menuPanel.add(btnViewTimetable);
        menuPanel.add(btnWorkload);
        menuPanel.add(btnSubstitute);
        menuPanel.add(btnCancel);
        menuPanel.add(btnClear);

        frame.add(menuPanel, BorderLayout.WEST);

        // Final UI Assembly & Boot
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
        System.out.println("Initializing UI Components...");
        System.out.println("Establishing Database Connection to MySQL...");
        DBConnection.getConnection();
        System.out.println("System Ready. Select an operation from the dashboard menu.\n");
    }

    // Helper Method to Style Buttons with Hover Effects
    private static JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }
}

// Custom helper class to route standard output to the JTextArea
class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    @Override
    public void write(byte[] b, int off, int len) {
        textArea.append(new String(b, off, len));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}