package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Student;
import com.example.repository.StudentRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }


    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByName(String name) {
        if (name == null || name.isBlank()) return findAll();

        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public Student findByMail(String mail) {
        if (mail == null || mail.isBlank()) return null;

        return studentRepository.findByMail(mail).orElse(null);
    }

    public long count() {
        return studentRepository.count();
    }

    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void delete(Student student) {
        studentRepository.delete(student);
    }

    public Student updateStudent(Long id, String name, String mail, LocalDate dob, String password) {
        Student student = findById(id);
        if (student != null) {
            student.setName(name);
            student.setMail(mail);
            student.setDob(dob);
            student.setPassword(password);
            return studentRepository.save(student);
        }
        return null;
    }

    public Student createStudent(String name, String mail, LocalDate dob, String password) {
        Student student = new Student();
        student.setName(name);
        student.setMail(mail);
        student.setDob(dob);
        student.setPassword(password);
        return studentRepository.save(student);
    }
}