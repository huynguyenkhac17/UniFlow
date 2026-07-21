package com.example.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "subjects")
public class Subject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjectId")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer credit;

    public Subject() {
    }

    public Subject(String name, Integer credit) {
        setName(name);
        setCredit(credit);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long subjectId) {
        this.id = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }
}
