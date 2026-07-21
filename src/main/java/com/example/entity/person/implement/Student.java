package com.example.entity.person.implement;

import com.example.entity.person.Person;
import com.example.entity.person.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "students")

public class Student extends Person {

    public Student() {
    }

    public Student(String name, String mail, LocalDate dob) {
        setName(name);
        setEmail(mail);
        setDob(dob);
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }
}
