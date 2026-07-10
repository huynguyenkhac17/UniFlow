package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.EnrollmentDto;
import com.example.entity.Enrollment;
import com.example.entity.EnrollmentId;
import com.example.entity.Student;
import com.example.entity.Subject;
import com.example.repository.EnrollmentRepository;

import jakarta.transaction.Transactional;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public long count() {
        return enrollmentRepository.count();
    }

//    // @Transactional giữ session mở trong lúc .map() để đọc được student/subject LAZY
//    @Transactional
//    public List<EnrollmentDto> findAllDto() {
//        return enrollmentRepository.findAll().stream()
//            .map(e -> new EnrollmentDto(
//                e.getStudent().getId(), e.getSubject().getId(),
//                e.getStudent().getName(), e.getSubject().getName(),
//                e.getSemester(), e.getGrade()))
//            .toList();
//    }

    // Không cần transaction nữa vì ko còn lazy request
    public List<EnrollmentDto> findAllDto() {
        return enrollmentRepository.findAllEnrollmentDTO();
    }

    public Enrollment save(Student student, Subject subject, String semester, Double grade) {
        Enrollment e = new Enrollment(student, subject, semester);
        e.setGrade(grade);
        return enrollmentRepository.save(e); // key trùng -> UPDATE, key mới -> INSERT
    }

    public void delete(EnrollmentId id) {
        enrollmentRepository.deleteById(id);
    }

    // true nếu sinh viên còn đang đăng ký
    public boolean isStudentEnrolled(Long studentId) {
        return enrollmentRepository.existsByStudentId(studentId);
    }

    public boolean isSubjectEnrolled(Long subjectId) {
        return enrollmentRepository.existsBySubjectId(subjectId);
    }
}