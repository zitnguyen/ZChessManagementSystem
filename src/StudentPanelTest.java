import database.DatabaseInit;
import javax.swing.*;
import ui.StudentPanel;

public class StudentPanelTest {
    public static void main(String[] args) {
        // Khởi tạo database
        DatabaseInit.initDatabase();
        System.out.println("Testing StudentPanel...");
        
        // Thiết lập Look and Feel của hệ thống
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Tạo JFrame chứa StudentPanel
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TEST: Student Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 600);
            frame.setLocationRelativeTo(null);
            
            // Add StudentPanel vào Frame
            StudentPanel panel = new StudentPanel();
            frame.add(panel);
            
            frame.setVisible(true);
            
            System.out.println("StudentPanel is running!");
        });
    }
}
