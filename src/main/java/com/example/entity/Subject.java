package com.example.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "subjects")
public class Subject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer credit;

    public Subject() {
    }

    public Subject(Long id, String name, String credit) {
        this.id = id;
        this.name = name;
        this.credit = Integer.parseInt(credit);
    }

    public Long getId() { return id;}
    public void setId(Long subjectId) { this.id = subjectId;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public Integer getCredit() { return credit;}
    public void setCredit(Integer credit) { this.credit = credit;}
}
