package models;

import java.time.LocalDate;

public class ChessClass {
    private int classId;
    private String className;
    private int teacherId;
    private String teacherName;
    private String schedule;
    private String room;
    private int maxStudents;
    private double feePerMonth;
    private LocalDate startDate;
    private String status; // active, completed, cancelled
    
    public ChessClass(int classId, String className, int teacherId, 
                      String schedule, String room, int maxStudents,
                      double feePerMonth, LocalDate startDate, String status) {
        this.classId = classId;
        this.className = className;
        this.teacherId = teacherId;
        this.schedule = schedule;
        this.room = room;
        this.maxStudents = maxStudents;
        this.feePerMonth = feePerMonth;
        this.startDate = startDate;
        this.status = status;
    }

    public ChessClass(String className, int teacherId, String schedule, 
                      String room, int maxStudents, double feePerMonth, 
                      LocalDate startDate) {
        this.className = className;
        this.teacherId = teacherId;
        this.schedule = schedule;
        this.room = room;
        this.maxStudents = maxStudents;
        this.feePerMonth = feePerMonth;
        this.startDate = startDate;
        this.status = "active";
    }

    public int getClassId() { return classId; }
    public String getClassName() { return className; }
    public int getTeacherId() { return teacherId; }
    public String getTeacherName() { return teacherName; }
    public String getSchedule() { return schedule; }
    public String getRoom() { return room; }
    public int getMaxStudents() { return maxStudents; }
    public double getFeePerMonth() { return feePerMonth; }
    public LocalDate getStartDate() { return startDate; }
    public String getStatus() { return status; }

    public void setClassId(int classId) { this.classId = classId; }
    public void setClassName(String className) { this.className = className; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public void setRoom(String room) { this.room = room; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
    public void setFeePerMonth(double feePerMonth) { this.feePerMonth = feePerMonth; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return className;
    }
}