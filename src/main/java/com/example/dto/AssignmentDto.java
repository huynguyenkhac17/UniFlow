package com.example.dto;

public class AssignmentDto {
    private final Long teacherId;      // giữ khóa để deleteAccount
    private final Long subjectId;
    private final String teacherName;  // để hiển thị
    private final String subjectName;

    public AssignmentDto(Long teacherId, Long subjectId, String teacherName, String subjectName) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.teacherName = teacherName;
        this.subjectName = subjectName;
    }

    public Long getTeacherId() { return teacherId; }
    public Long getSubjectId() { return subjectId; }
    public String getTeacherName() { return teacherName; }
    public String getSubjectName() { return subjectName; }
}
