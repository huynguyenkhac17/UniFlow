package com.example.service;

import org.springframework.stereotype.Service;
import com.example.entity.Teacher;
import com.example.repository.TeacherRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher findById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Teacher findByMail(String mail) {
        if (mail == null || mail.isBlank()) return null;

        return teacherRepository.findByMail(mail).orElse(null);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public List<Teacher> findByName(String name) {
        if (name == null || name.isBlank()) return findAll();

        return teacherRepository.findByNameContainingIgnoreCase(name);
    }

    public long count() {
        return teacherRepository.count();
    }

    @Transactional
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void delete(Teacher teacher) {
        teacherRepository.delete(teacher);
    }

    public Teacher updateTeacher(Long id, String name, String mail, String department, String password) {
        Teacher teacher = findById(id);
        if (teacher != null) {
            teacher.setName(name);
            teacher.setMail(mail);
            teacher.setDepartment(department);
            teacher.setPassword(password);
            return teacherRepository.save(teacher);
        }
        return null;
    }

    public Teacher createTeacher(String name, String mail, String department, String password) {
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setMail(mail);
        teacher.setDepartment(department);
        teacher.setPassword(password);
        return teacherRepository.save(teacher);
    }
}
