package com.example.entity.person;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// Join khi truy vấn, bảng chuẩn hóa || SINGLER_TABLE: chỉ sinh một bảng cho mỗi entity, truy vấn nhanh, nhưng bảng rỗng, nhiều null
public abstract class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không được để trống!")
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @NotBlank(message = "Email không được để trống!")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    public abstract Role getRole();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
