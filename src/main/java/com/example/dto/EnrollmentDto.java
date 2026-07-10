package com.example.dto;

public class EnrollmentDto {
    private final Long studentId;      // giữ khóa để edit/delete
    private final Long subjectId;
    private final String studentName;  // để hiển thị
    private final String subjectName;
    private final String semester;
    private final Double grade;

    public EnrollmentDto(Long studentId, Long subjectId, String studentName, String subjectName, String semester, Double grade) {
        this.studentId = studentId;   
        this.subjectId = subjectId;
        this.studentName = studentName; 
        this.subjectName = subjectName;
        this.semester = semester;     
        this.grade = grade;
    }
    public Long getStudentId() { return studentId; }
    public Long getSubjectId() { return subjectId; }
    public String getStudentName() { return studentName; }
    public String getSubjectName() { return subjectName; }
    public String getSemester() { return semester; }
    public Double getGrade() { return grade; }
}