package com.example.entity.person.implement;

import com.example.entity.person.Person;
import com.example.entity.person.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher extends Person {

    @Column(name = "department")
    private String department;

    public Teacher() {
    }

    public Teacher(String name, String mail, String department) {
        setName(name);
        setEmail(mail);
        setDepartment(department);
    }

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }


    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
