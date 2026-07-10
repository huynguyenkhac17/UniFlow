package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EnrollmentId implements Serializable {
    
    @Column(name = "studentId")
    private Long studentId;

    @Column(name = "subjectId")
    private Long subjectId;

    @Column(name = "semester")
    private String semester;

    public EnrollmentId() {
    }

    public EnrollmentId(Long studentId, Long subjectId, String semester) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.semester = semester;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentId)) return false;

        EnrollmentId that = (EnrollmentId) o;

        return Objects.equals(studentId, that.studentId) && Objects.equals(subjectId, that.subjectId) && Objects.equals(semester, that.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, subjectId, semester);
    }
}
