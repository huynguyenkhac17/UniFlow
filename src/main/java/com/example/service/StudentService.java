package com.example.service;

import com.example.entity.person.implement.Student;
import com.example.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final AppUserService appUserService;

    public StudentService(StudentRepository studentRepository, AppUserService appUserService) {
        this.studentRepository = studentRepository;
        this.appUserService = appUserService;
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
        Student saved = studentRepository.save(student);  // ko đc sync trước vì student chưa chắc đã tạo, có thể chưa có trong db sẽ lỗi
        appUserService.sync(student); // cập nhật tài khoản nếu lưu thông tin
        return saved;
    }

    @Transactional
    public void delete(Student student) {
        appUserService.deleteAccount(student); // xóa tài khoản nếu xóa sinh viên.
        studentRepository.delete(student);
    }

}