package models;

import java.time.LocalDate;

public class Student {
    private int studentId;
    private String fullName;
    private LocalDate birthDate;
    private String phone;
    private String parentPhone;
    private String email;
    private String address;
    private LocalDate enrollmentDate;
    private String status; // active, inactive, graduated
    private String notes;
    
    public Student(int studentId, String fullName, LocalDate birthDate, 
                   String phone, String parentPhone, String email, 
                   String address, LocalDate enrollmentDate, 
                   String status, String notes) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.parentPhone = parentPhone;
        this.email = email;
        this.address = address;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.notes = notes;
    }
    
    public Student(String fullName, LocalDate birthDate, 
                   String phone, String parentPhone, String email, 
                   String address, String notes) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.parentPhone = parentPhone;
        this.email = email;
        this.address = address;
        this.enrollmentDate = LocalDate.now();
        this.status = "active";
        this.notes = notes;
    }
    
    public int getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }
    public String getParentPhone() { return parentPhone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    
    @Override
    public String toString() {
        return fullName; 
    }
}