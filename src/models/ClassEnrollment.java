package models;

import java.time.LocalDate;

public class ClassEnrollment {
    private int enrollmentId;
    private int studentId;
    private String studentName; 
    private int classId;
    private String className; 
    private LocalDate enrollmentDate;
    private String status; // active, completed, dropped
    
    public ClassEnrollment(int enrollmentId, int studentId, int classId,
                          LocalDate enrollmentDate, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.classId = classId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public ClassEnrollment(int studentId, int classId) {
        this.studentId = studentId;
        this.classId = classId;
        this.enrollmentDate = LocalDate.now();
        this.status = "active";
    }
 
    public int getEnrollmentId() { return enrollmentId; }
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getClassId() { return classId; }
    public String getClassName() { return className; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public String getStatus() { return status; }
   
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setClassId(int classId) { this.classId = classId; }
    public void setClassName(String className) { this.className = className; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public void setStatus(String status) { this.status = status; }
}