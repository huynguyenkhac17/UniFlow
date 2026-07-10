package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AssignmentId implements Serializable{
    
    @Column(name = "teacherId")
    private Long teacherId;

    @Column(name = "subjectId")
    private Long subjectId;

    public AssignmentId() {
    }

    public AssignmentId(Long teacherId, Long subjectId) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignmentId)) return false;

        AssignmentId that = (AssignmentId) o;

        return Objects.equals(teacherId, that.teacherId) && Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, subjectId);
    }
}
