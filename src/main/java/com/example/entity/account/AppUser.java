package com.example.entity.account;

import com.example.entity.person.Role;
import com.example.entity.person.Person;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "app_users")

public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // làm username

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // mặc định là đánh số
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId", unique = true)
    private Person person;

    public AppUser(String username, String passwordHash, Role role) {
        setUsername(username);
        setPasswordHash(passwordHash);
        this.role = role;
    }

    public AppUser() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}