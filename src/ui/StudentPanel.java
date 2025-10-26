package ui;

import dao.StudentDAO;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class StudentPanel extends JPanel {

    private final StudentDAO studentDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtFullName, txtBirthDate, txtParentPhone, txtEmail, txtAddress, txtSearch;
    private JTextArea txtNotes;
    private JComboBox<String> cboStatus;

    private int selectedStudentId = -1;

    public StudentPanel() {
        studentDAO = new StudentDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initComponents();
        loadStudentsData();
    }

    // ======= INIT UI =======
    private void initComponents() {
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.EAST);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("📚 QUẢN LÝ HỌC VIÊN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSearch = createStyledButton("🔍 Tìm", new Color(46, 204, 113));
        btnSearch.addActionListener(e -> searchStudents());

        JButton btnRefresh = createStyledButton("🔄 Làm mới", new Color(52, 73, 94));
        btnRefresh.addActionListener(e -> loadStudentsData());

        searchPanel.add(searchLabel);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);

        panel.add(searchPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {
                "ID", "Họ tên", "Ngày sinh", "SĐT phụ huynh",
                "Email", "Ngày nhập học", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadStudentToForm(table.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách học viên"
        ));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Thông tin học viên"));

        txtFullName = new JTextField(20);
        txtBirthDate = new JTextField(20);
        txtParentPhone = new JTextField(20);
        txtEmail = new JTextField(20);
        txtAddress = new JTextField(20);
        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);

        String[] statuses = {"active", "inactive", "graduated"};
        cboStatus = new JComboBox<>(statuses);

        panel.add(createFormRow("Họ tên (*)", txtFullName));
        panel.add(createFormRow("Ngày sinh (dd/MM/yyyy)", txtBirthDate));
        panel.add(createFormRow("SĐT phụ huynh", txtParentPhone));
        panel.add(createFormRow("Email", txtEmail));
        panel.add(createFormRow("Địa chỉ", txtAddress));
        panel.add(createFormRow("Trạng thái", cboStatus));

        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.add(new JLabel("Ghi chú:"), BorderLayout.NORTH);
        notesPanel.add(new JScrollPane(txtNotes), BorderLayout.CENTER);
        panel.add(notesPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createButtonPanel());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createFormRow(String label, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 25));
        row.add(lbl, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton btnAdd = createStyledButton("➕ Thêm mới", new Color(46, 204, 113));
        btnAdd.addActionListener(e -> addStudent());

        JButton btnUpdate = createStyledButton("✏️ Cập nhật", new Color(52, 152, 219));
        btnUpdate.addActionListener(e -> updateStudent());

        JButton btnDelete = createStyledButton("🗑️ Xóa", new Color(231, 76, 60));
        btnDelete.addActionListener(e -> deleteStudent());

        JButton btnClear = createStyledButton("🔄 Làm mới form", new Color(149, 165, 166));
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ======= DATA HANDLERS =======
    private void loadStudentsData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getFullName(),
                    s.getBirthDate() != null ? s.getBirthDate().format(fmt) : "",
                    s.getParentPhone(),
                    s.getEmail(),
                    s.getEnrollmentDate() != null ? s.getEnrollmentDate().format(fmt) : "",
                    s.getStatus()
            });
        }
        clearForm();
    }

    private void loadStudentToForm(int rowIndex) {
        selectedStudentId = (int) tableModel.getValueAt(rowIndex, 0);
        Student s = studentDAO.getStudentById(selectedStudentId);

        if (s != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtFullName.setText(s.getFullName());
            txtBirthDate.setText(s.getBirthDate() != null ? s.getBirthDate().format(fmt) : "");
            txtParentPhone.setText(s.getParentPhone());
            txtEmail.setText(s.getEmail());
            txtAddress.setText(s.getAddress());
            cboStatus.setSelectedItem(s.getStatus());
            txtNotes.setText(s.getNotes());
        }
    }

    private void addStudent() {
        if (!validateInput()) return;

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birth = txtBirthDate.getText().trim().isEmpty() ? null
                    : LocalDate.parse(txtBirthDate.getText().trim(), fmt);

            LocalDate enrollment = LocalDate.now();

            Student s = new Student(
                    txtFullName.getText().trim(),
                    birth,
                    txtParentPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim(),
                    enrollment,
                    (String) cboStatus.getSelectedItem(),
                    txtNotes.getText().trim()
            );

            if (studentDAO.addStudent(s)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm học viên thành công!");
                clearForm();
                loadStudentsData();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "❌ Sai định dạng ngày (dd/MM/yyyy)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn học viên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birth = txtBirthDate.getText().trim().isEmpty() ? null
                    : LocalDate.parse(txtBirthDate.getText().trim(), fmt);

            Student s = studentDAO.getStudentById(selectedStudentId);
            s.setFullName(txtFullName.getText().trim());
            s.setBirthDate(birth);
            s.setParentPhone(txtParentPhone.getText().trim());
            s.setEmail(txtEmail.getText().trim());
            s.setAddress(txtAddress.getText().trim());
            s.setStatus((String) cboStatus.getSelectedItem());
            s.setNotes(txtNotes.getText().trim());

            if (studentDAO.updateStudent(s)) {
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                clearForm();
                loadStudentsData();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "❌ Sai định dạng ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng chọn học viên để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa học viên này? (sẽ chuyển sang inactive)",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION && studentDAO.deleteStudent(selectedStudentId)) {
            JOptionPane.showMessageDialog(this, "✅ Xóa thành công!");
            clearForm();
            loadStudentsData();
        }
    }

    private void searchStudents() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadStudentsData();
            return;
        }

        tableModel.setRowCount(0);
        List<Student> list = studentDAO.searchStudentsByName(keyword);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getFullName(),
                    s.getBirthDate() != null ? s.getBirthDate().format(fmt) : "",
                    s.getParentPhone(),
                    s.getEmail(),
                    s.getEnrollmentDate() != null ? s.getEnrollmentDate().format(fmt) : "",
                    s.getStatus()
            });
        }
        clearForm();
    }

    private boolean validateInput() {
        if (txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Họ tên không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        selectedStudentId = -1;
        txtFullName.setText("");
        txtBirthDate.setText("");
        txtParentPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        cboStatus.setSelectedIndex(0);
        txtNotes.setText("");
        table.clearSelection();
    }
}
