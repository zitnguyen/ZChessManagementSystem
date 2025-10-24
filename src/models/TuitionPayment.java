package models;

import java.time.LocalDate;

public class TuitionPayment {
    private int paymentId;
    private int studentId;
    private String studentName; 
    private int classId;
    private String className; 
    private double amount;
    private LocalDate paymentDate;
    private String monthYear; 
    private String paymentMethod; // cash, bank_transfer, momo
    private String status; // paid, pending, cancelled
    private String notes;
    
    public TuitionPayment(int paymentId, int studentId, int classId,
                         double amount, LocalDate paymentDate, String monthYear,
                         String paymentMethod, String status, String notes) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.classId = classId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.monthYear = monthYear;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.notes = notes;
    }
    
    public TuitionPayment(int studentId, int classId, double amount,
                         String monthYear, String paymentMethod, String notes) {
        this.studentId = studentId;
        this.classId = classId;
        this.amount = amount;
        this.paymentDate = LocalDate.now();
        this.monthYear = monthYear;
        this.paymentMethod = paymentMethod;
        this.status = "paid";
        this.notes = notes;
    }
    
    public int getPaymentId() { return paymentId; }
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getClassId() { return classId; }
    public String getClassName() { return className; }
    public double getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public String getMonthYear() { return monthYear; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setClassId(int classId) { this.classId = classId; }
    public void setClassName(String className) { this.className = className; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
}