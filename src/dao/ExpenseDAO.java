package dao;

import database.DatabaseManager;
import models.Expense;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    
    // Thêm chi phí mới
    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (category, description, amount, expense_date, " +
                    "payment_method, paid_to, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, expense.getCategory());
            pstmt.setString(2, expense.getDescription());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getExpenseDate().toString());
            pstmt.setString(5, expense.getPaymentMethod());
            pstmt.setString(6, expense.getPaidTo());
            pstmt.setString(7, expense.getNotes());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật chi phí
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET category = ?, description = ?, amount = ?, " +
                    "expense_date = ?, payment_method = ?, paid_to = ?, notes = ? " +
                    "WHERE expense_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, expense.getCategory());
            pstmt.setString(2, expense.getDescription());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getExpenseDate().toString());
            pstmt.setString(5, expense.getPaymentMethod());
            pstmt.setString(6, expense.getPaidTo());
            pstmt.setString(7, expense.getNotes());
            pstmt.setInt(8, expense.getExpenseId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa chi phí
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE expense_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy chi phí theo ID
    public Expense getExpenseById(int expenseId) {
        String sql = "SELECT * FROM expenses WHERE expense_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractExpenseFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy tất cả chi phí
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses ORDER BY expense_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                expenses.add(extractExpenseFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
    
    // Lấy chi phí theo khoảng thời gian
    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE expense_date BETWEEN ? AND ? " +
                    "ORDER BY expense_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                expenses.add(extractExpenseFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
    
    // Lấy chi phí theo danh mục
    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE category = ? ORDER BY expense_date DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                expenses.add(extractExpenseFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
    
    // Tính tổng chi phí
    public double getTotalExpenses() {
        String sql = "SELECT SUM(amount) FROM expenses";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Tính tổng chi phí theo khoảng thời gian
    public double getTotalExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(amount) FROM expenses WHERE expense_date BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Tính tổng chi phí theo danh mục
    public double getTotalExpensesByCategory(String category) {
        String sql = "SELECT SUM(amount) FROM expenses WHERE category = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Helper method
    private Expense extractExpenseFromResultSet(ResultSet rs) throws SQLException {
        return new Expense(
            rs.getInt("expense_id"),
            rs.getString("category"),
            rs.getString("description"),
            rs.getDouble("amount"),
            LocalDate.parse(rs.getString("expense_date")),
            rs.getString("payment_method"),
            rs.getString("paid_to"),
            rs.getString("notes")
        );