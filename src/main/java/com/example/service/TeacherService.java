package com.example.service;

import com.example.entity.Teacher;
import com.example.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final AppUserService appUserService;

    public TeacherService(TeacherRepository teacherRepository, AppUserService appUserService) {
        this.teacherRepository = teacherRepository;
        this.appUserService = appUserService;
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
        Teacher saved = teacherRepository.save(teacher);
        appUserService.sync(teacher);
        return saved;
    }

    @Transactional
    public void delete(Teacher teacher) {
        appUserService.deleteAccount(teacher);
        teacherRepository.delete(teacher);
    }

}
