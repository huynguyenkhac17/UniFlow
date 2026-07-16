package com.example.entity;

import com.example.entity.account.AccountOwner;
import com.example.entity.account.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Entity
@Table(name = "teachers")
public class Teacher implements Serializable, AccountOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long teacherId;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String mail;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "department")
    private String department;

    public Teacher() {
    }

    public Teacher(String name, String mail, String password, String department) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.department = department;
    }

    public Long getId() { return this.teacherId;}
    public void setId(Long teacherId) { this.teacherId = teacherId;}
    public String getName() { return this.name;}
    public void setName(String name) { this.name = name;}

    @Override
    public String getEmail() { return this.mail;}

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }

    @Override
    public void linkTo(AppUser user) {
        user.setTeacher(this);
    }

    public void setEmail(String mail) { this.mail = mail;}
    public String getPassword() { return this.password;}
    public void setPassword(String password) { this.password = password;}
    public String getDepartment() { return this.department;}
    public void setDepartment(String department) { this.department = department;}

}
