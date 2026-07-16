package com.example.entity;

import com.example.entity.account.AccountOwner;
import com.example.entity.account.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student implements Serializable, AccountOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Tên không được để trống!")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email sai định dạng!")
    @Column(name = "mail",  unique = true)
    private String mail;

    @Column(name = "password")
    private String password;

    @Column(name = "dob")
    private LocalDate dob;

    public Student() {
    }

    public Student(String name, String mail, String password, LocalDate dob) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.dob = dob;
    }

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    @Override
    public String getEmail() { return mail;}
    public void setEmail(String mail) { this.mail = mail;}
    public String getPassword() { return password;}
    public void setPassword(String password) { this.password = password;}
    public LocalDate getDob() { return dob;}
    public void setDob(LocalDate dob) { this.dob = dob;}

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

    @Override
    public void linkTo(AppUser user) {
        user.setStudent(this);
    }
}
