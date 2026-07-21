package com.example.repository;

import com.example.entity.person.implement.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Implement repository methods for Student entity
    
    Optional<Student> findByMail(String mail);
    
    Optional<Student> findById(Long id);

    List<Student> findByNameContainingIgnoreCase(String name);
}
