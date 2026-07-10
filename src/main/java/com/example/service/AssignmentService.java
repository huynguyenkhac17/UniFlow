package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.AssignmentDto;
import com.example.entity.Assignment;
import com.example.entity.AssignmentId;
import com.example.entity.Subject;
import com.example.entity.Teacher;
import com.example.repository.AssignmentRepository;

import jakarta.transaction.Transactional;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public long count() {
        return assignmentRepository.count();
    }

    // @Transactional giữ session mở trong lúc .map() để đọc được teacher/subject LAZY
    @Transactional
    public List<AssignmentDto> findAllDto() {
        return assignmentRepository.findAll().stream()
            .map(a -> new AssignmentDto(
                a.getTeacher().getId(), a.getSubject().getId(),
                a.getTeacher().getName(), a.getSubject().getName()))
            .toList();
    }

    public Assignment save(Teacher teacher, Subject subject) {
        Assignment a = new Assignment(teacher, subject);
        return assignmentRepository.save(a); // save theo khóa chính: trùng -> update, mới -> insert
    }

    public void delete(AssignmentId id) {
        assignmentRepository.deleteById(id);
    }

    // true nếu giảng viên còn phân công -> không cho xóa giảng viên
    public boolean isTeacherAssigned(Long teacherId) {
        return assignmentRepository.existsByTeacherId(teacherId);
    }

    // true nếu môn còn được phân công dạy -> không cho xóa môn
    public boolean isSubjectAssigned(Long subjectId) {
        return assignmentRepository.existsBySubjectId(subjectId);
    }
}
