package com.example.service;

import org.springframework.stereotype.Service;
import com.example.entity.Subject;
import com.example.repository.SubjectRepository;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public long count() {
        return subjectRepository.count();
    }

    @Transactional
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Transactional
    public void delete(Subject subject) {
        subjectRepository.delete(subject);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public List<Subject> findByName(String name) {
        if (name == null || name.isBlank()) return findAll();

        return subjectRepository.findByNameContainingIgnoreCase(name);
    }

    public Subject createSubject(String name, Integer credit) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setCredit(credit);
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, String name, Integer credit) {
        Subject subject = findById(id);
        if (subject != null) {
            subject.setName(name);
            subject.setCredit(credit);
            return subjectRepository.save(subject);
        }
        return null;
    }
}
