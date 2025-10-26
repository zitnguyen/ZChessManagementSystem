package ui;

import dao.StudentDAO;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.Student;

public class StudentPanel extends JPanel {

    private final StudentDAO studentDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtFullName, txtBirthDate, txtParentPhone, txtEmail, txtAddress, txtEnrollmentDate, txtSearch;
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

    private void initComponents() {
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.EAST);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("QUẢN LÝ HỌC VIÊN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.black);
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setForeground(Color.black);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.addActionListener(e -> searchStudents());

        JButton btnSearch = createStyledButton("Tìm", new Color(46, 204, 113));
        btnSearch.addActionListener(e -> searchStudents());

        JButton btnRefresh = createStyledButton("Làm mới", new Color(52, 73, 94));
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
            "ID", "Họ tên", "Ngày sinh", "SĐT PH", "Email", "Ngày nhập học", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.black);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ĐỔI MÀU HÀNG THEO TRẠNG THÁI
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) table.getValueAt(row, 6);

                if (!isSelected) {
                    if ("inactive".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 255, 150));
                    } else if ("graduated".equalsIgnoreCase(status)) {
                        c.setBackground(Color.red);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(new Color(135, 206, 250));
                }
                return c;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadStudentToForm(table.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách học viên"));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin học viên"));

        txtFullName = new JTextField(20);
        txtBirthDate = new JTextField(20);
        txtParentPhone = new JTextField(20);
        txtEmail = new JTextField(20);
        txtAddress = new JTextField(20);
        txtEnrollmentDate = new JTextField(20); // 🆕 thêm ô nhập Ngày nhập học
        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);

        cboStatus = new JComboBox<>(new String[]{"active", "inactive", "graduated"});

        panel.add(createFormRow("Họ tên (*)", txtFullName));
        panel.add(createFormRow("Ngày sinh (dd/MM/yyyy)", txtBirthDate));
        panel.add(createFormRow("SĐT phụ huynh", txtParentPhone));
        panel.add(createFormRow("Email", txtEmail));
        panel.add(createFormRow("Địa chỉ", txtAddress));
        panel.add(createFormRow("Ngày nhập học (dd/MM/yyyy)", txtEnrollmentDate)); // 🆕 thêm vào form
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
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(140, 25));
        row.add(lbl, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return row;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton btnAdd = createStyledButton("Thêm mới", new Color(52, 152, 219));
        btnAdd.addActionListener(e -> addStudent());

        JButton btnUpdate = createStyledButton("Cập nhật", new Color(46, 204, 113));
        btnUpdate.addActionListener(e -> updateStudent());

        JButton btnDelete = createStyledButton("Xóa", new Color(231, 76, 60));
        btnDelete.addActionListener(e -> deleteStudent());

        JButton btnClear = createStyledButton("Làm mới form", new Color(149, 165, 166));
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =============================
    // CÁC XỬ LÝ NGHIỆP VỤ
    // =============================

    private void addStudent() {
        if (!validateInput()) return;
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birth = txtBirthDate.getText().isEmpty() ? null :
                    LocalDate.parse(txtBirthDate.getText().trim(), fmt);

            LocalDate enrollment = txtEnrollmentDate.getText().isEmpty() ? LocalDate.now() :
                    LocalDate.parse(txtEnrollmentDate.getText().trim(), fmt); // 🆕 thêm dòng này

            Student s = new Student(
                    txtFullName.getText().trim(),
                    birth,
                    txtParentPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim(),
                    txtNotes.getText().trim()
            );
            s.setStatus((String) cboStatus.getSelectedItem());
            s.setEnrollmentDate(enrollment); // 🆕 cập nhật enrollment

            if (studentDAO.addStudent(s)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm học viên thành công!");
                loadStudentsData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Sai định dạng ngày (dd/MM/yyyy)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học viên để cập nhật!");
            return;
        }
        if (!validateInput()) return;

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birth = txtBirthDate.getText().isEmpty() ? null :
                    LocalDate.parse(txtBirthDate.getText().trim(), fmt);

            LocalDate enrollment = txtEnrollmentDate.getText().isEmpty() ? LocalDate.now() :
                    LocalDate.parse(txtEnrollmentDate.getText().trim(), fmt); // 🆕 thêm dòng này

            Student s = studentDAO.getStudentById(selectedStudentId);
            if (s == null) return;

            s.setFullName(txtFullName.getText().trim());
            s.setBirthDate(birth);
            s.setParentPhone(txtParentPhone.getText().trim());
            s.setEmail(txtEmail.getText().trim());
            s.setAddress(txtAddress.getText().trim());
            s.setStatus((String) cboStatus.getSelectedItem());
            s.setNotes(txtNotes.getText().trim());
            s.setEnrollmentDate(enrollment); // 🆕 cập nhật enrollment

            if (studentDAO.updateStudent(s)) {
                JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                loadStudentsData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Sai định dạng ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học viên để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this, "Bạn có chắc muốn xóa học viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION && studentDAO.deleteStudent(selectedStudentId)) {
            loadStudentsData();
            clearForm();
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
        table.clearSelection();
        selectedStudentId = -1;
    }

    private void loadStudentsData() {
        tableModel.setRowCount(0);
        List<Student> list = studentDAO.getAllStudents();
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
        table.clearSelection();
        selectedStudentId = -1;
    }

    private void loadStudentToForm(int row) {
        selectedStudentId = (int) tableModel.getValueAt(row, 0);
        Student s = studentDAO.getStudentById(selectedStudentId);
        if (s == null) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtFullName.setText(s.getFullName());
        txtBirthDate.setText(s.getBirthDate() != null ? s.getBirthDate().format(fmt) : "");
        txtParentPhone.setText(s.getParentPhone());
        txtEmail.setText(s.getEmail());
        txtAddress.setText(s.getAddress());
        txtEnrollmentDate.setText(s.getEnrollmentDate() != null ? s.getEnrollmentDate().format(fmt) : ""); // 🆕 hiển thị ngày nhập học
        cboStatus.setSelectedItem(s.getStatus());
        txtNotes.setText(s.getNotes());
    }

    private void clearForm() {
        selectedStudentId = -1;
        txtFullName.setText("");
        txtBirthDate.setText("");
        txtParentPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtEnrollmentDate.setText(""); // 🆕 reset luôn ô này
        cboStatus.setSelectedIndex(0);
        txtNotes.setText("");
        table.clearSelection();
    }

    private boolean validateInput() {
        if (txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Họ tên không được để trống!");
            return false;
        }
        return true;
    }
}
