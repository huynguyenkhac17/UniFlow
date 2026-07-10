package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "assignments")
public class Assignment implements Serializable {

    @EmbeddedId
    private AssignmentId id;
    
    @MapsId("teacherId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherid")
    private Teacher teacher;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectid")
    private Subject subject;

    public Assignment() {
    }

    public Assignment(Teacher teacher, Subject subject) {
        this.teacher = teacher;
        this.subject = subject;
        this.id = new AssignmentId(teacher.getId(), subject.getId());
    }

    public AssignmentId getId() { return id;}
    public void setId(AssignmentId id) { this.id = id;}
    public Teacher getTeacher() { return teacher;}
    public void setTeacher(Teacher teacher) { this.teacher = teacher;}
    public Subject getSubject() { return subject;}
    public void setSubject(Subject subject) { this.subject = subject;}
}
