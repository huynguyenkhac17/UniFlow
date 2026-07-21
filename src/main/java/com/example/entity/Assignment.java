package com.example.entity;

import com.example.entity.person.implement.Teacher;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "assignments")
public class Assignment implements Serializable {

    @EmbeddedId
    private AssignmentId id;

    @MapsId("teacherId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId")
    private Subject subject;

    public Assignment() {
    }

    public Assignment(Teacher teacher, Subject subject) {
        setTeacher(teacher);
        setSubject(subject);
        setId(new AssignmentId(teacher.getId(), subject.getId()));
    }

    public AssignmentId getId() {
        return id;
    }

    public void setId(AssignmentId id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
