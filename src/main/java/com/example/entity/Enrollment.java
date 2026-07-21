package com.example.entity;

import java.io.Serializable;

import com.example.entity.person.implement.Student;
import jakarta.persistence.*;


@Entity
@Table(name = "enrollments")
public class Enrollment implements Serializable {

    @EmbeddedId
    private EnrollmentId id;

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId")
    private Subject subject;

    @Column(name = "grade")
    private Double grade;

    public Enrollment() {
    }

    public Enrollment(Student student, Subject subject, String semester) {
        setStudent(student);
        setSubject(subject);
        setId(new EnrollmentId(student.getId(), subject.getId(), semester));
    }

    public EnrollmentId getId() {
        return this.id;
    }

    public void setId(EnrollmentId id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getSemester() {
        return id != null ? id.getSemester() : null;
    }
}
