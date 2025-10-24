package models;

import java.time.LocalDate;

public class Expense {
    private int expenseId;
    private String category; // teacher_salary, rent, utilities, equipment, marketing, other
    private String description;
    private double amount;
    private LocalDate expenseDate;
    private String paymentMethod;
    private String paidTo;
    private String notes;
    
    public Expense(int expenseId, String category, String description,
                   double amount, LocalDate expenseDate, String paymentMethod,
                   String paidTo, String notes) {
        this.expenseId = expenseId;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.paymentMethod = paymentMethod;
        this.paidTo = paidTo;
        this.notes = notes;
    }
    
    public Expense(String category, String description, double amount,
                   LocalDate expenseDate, String paymentMethod,
                   String paidTo, String notes) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.paymentMethod = paymentMethod;
        this.paidTo = paidTo;
        this.notes = notes;
    }
   
    public int getExpenseId() { return expenseId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaidTo() { return paidTo; }
    public String getNotes() { return notes; }
    
    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaidTo(String paidTo) { this.paidTo = paidTo; }
    public void setNotes(String notes) { this.notes = notes; }
}