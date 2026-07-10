package com.example.repository;

import com.example.entity.Enrollment;
import com.example.entity.EnrollmentId;
import com.example.dto.EnrollmentDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    // Implement repository methods for Enrollment entity
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findBySubjectId(Long subjectId);

    List<Enrollment> findByIdSemester(String semester);


    @Query("""
    select new com.example.dto.EnrollmentDto(
        st.id, sj.id, st.name, sj.name, e.id.semester, e.grade)
    from Enrollment e
    join e.student st
    join e.subject sj
    """)
    List<EnrollmentDto> findAllEnrollmentDTO();

    Optional<Enrollment> findById(EnrollmentId id);

    boolean existsByStudentId(Long studentId);

    boolean existsBySubjectId(Long subjectId);
}
