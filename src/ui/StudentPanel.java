package ui;

import java.awt.*;
import javax.swing.*;

public class StudentPanel extends JPanel {
    public StudentPanel() {
        setLayout(new BorderLayout());
        
        JLabel label = new JLabel("📚 QUẢN LÝ HỌC VIÊN", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
        
        JLabel subLabel = new JLabel("Chức năng đang phát triển...", SwingConstants.CENTER);
        subLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(subLabel, BorderLayout.SOUTH);
    }
}