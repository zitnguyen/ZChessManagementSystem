package models;

import java.time.LocalDate;

public class Teacher {
    private int teacherId;
    private String fullName;
    private String phone;
    private String email;
    private double salaryPerSession;
    private LocalDate hireDate;
    private String status; // active, inactive
    private String specialization;
    private String notes;
  
    public Teacher(int teacherId, String fullName, String phone, 
                   String email, double salaryPerSession, LocalDate hireDate,
                   String status, String specialization, String notes) {
        this.teacherId = teacherId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.salaryPerSession = salaryPerSession;
        this.hireDate = hireDate;
        this.status = status;
        this.specialization = specialization;
        this.notes = notes;
    }
 
    public Teacher(String fullName, String phone, String email, 
                   double salaryPerSession, String specialization, String notes) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.salaryPerSession = salaryPerSession;
        this.hireDate = LocalDate.now();
        this.status = "active";
        this.specialization = specialization;
        this.notes = notes;
    }

    public int getTeacherId() { return teacherId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public double getSalaryPerSession() { return salaryPerSession; }
    public LocalDate getHireDate() { return hireDate; }
    public String getStatus() { return status; }
    public String getSpecialization() { return specialization; }
    public String getNotes() { return notes; }
    
 
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setSalaryPerSession(double salaryPerSession) { this.salaryPerSession = salaryPerSession; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public void setStatus(String status) { this.status = status; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setNotes(String notes) { this.notes = notes; }
    
    @Override
    public String toString() {
        return fullName; 
    }
}