package com.example.repository;

import com.example.entity.person.implement.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // Implement repository methods for Teacher entity
    
    Optional<Teacher> findByMail(String mail);

    List<Teacher> findByNameContainingIgnoreCase(String name);
}
