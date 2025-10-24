package models;

import java.time.LocalDate;

public class StudentProgress {
    private int progressId;
    private int studentId;
    private String studentName; 
    private LocalDate assessmentDate;
    private String skillLevel; // beginner, intermediate, advanced
    private int eloRating;
    private String achievements;
 
    public StudentProgress(int progressId, int studentId, LocalDate assessmentDate,
                          String skillLevel, int eloRating, String achievements,
                          String notes) {
        this.progressId = progressId;
        this.studentId = studentId;
        this.assessmentDate = assessmentDate;
        this.skillLevel = skillLevel;
        this.eloRating = eloRating;
        this.achievements = achievements;
        this.notes = notes;
    }

    public StudentProgress(int studentId, String skillLevel, int eloRating,
                          String achievements, String notes) {
        this.studentId = studentId;
        this.assessmentDate = LocalDate.now();
        this.skillLevel = skillLevel;
        this.eloRating = eloRating;
        this.achievements = achievements;
        this.notes = notes;
    }
 
    public int getProgressId() { return progressId; }
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public LocalDate getAssessmentDate() { return assessmentDate; }
    public String getSkillLevel() { return skillLevel; }
    public int getEloRating() { return eloRating; }
    public String getAchievements() { return achievements; }
    public String getNotes() { return notes; }
    

    public void setProgressId(int progressId) { this.progressId = progressId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setAssessmentDate(LocalDate assessmentDate) { this.assessmentDate = assessmentDate; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
    public void setEloRating(int eloRating) { this.eloRating = eloRating; }
    public void setAchievements(String achievements) { this.achievements = achievements; }
    public void setNotes(String notes) { this.notes = notes; }
}