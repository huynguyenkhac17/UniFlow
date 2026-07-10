package com.example.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Assignment;
import com.example.entity.AssignmentId;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, AssignmentId> {
    List<Assignment> findByTeacherId(Long teacherId);

    List<Assignment> findBySubjectId(Long subjectId);

    boolean existsByTeacherId(Long teacherId);

    boolean existsBySubjectId(Long subjectId);
}
