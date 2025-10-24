package models;

import java.time.LocalDate;

public class Attendance {
    private int attendanceId;
    private int classId;
    private String className; 
    private int studentId;
    private String studentName;
    private LocalDate attendanceDate;
    private String status; // present, absent, late, excused
    private String notes;
    
    public Attendance(int attendanceId, int classId, int studentId,
                     LocalDate attendanceDate, String status, String notes) {
        this.attendanceId = attendanceId;
        this.classId = classId;
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.notes = notes;
    }
    
    public Attendance(int classId, int studentId, LocalDate attendanceDate,
                     String status, String notes) {
        this.classId = classId;
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.notes = notes;
    }
  
    public int getAttendanceId() { return attendanceId; }
    public int getClassId() { return classId; }
    public String getClassName() { return className; }
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    public void setClassId(int classId) { this.classId = classId; }
    public void setClassName(String className) { this.className = className; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
}