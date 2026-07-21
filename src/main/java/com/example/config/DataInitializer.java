package com.example.config;

import com.example.entity.*;
import com.example.entity.account.AppUser;
import com.example.entity.person.Role;
import com.example.entity.person.implement.Student;
import com.example.entity.person.implement.Teacher;
import com.example.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * sinh dữ liệu khi khởi động app.
 * Spring gọi run(...) 1  lần sau khi context sẵn sàng.
 * Chỉ seed khi bảng đang trống
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder encoder;

    public DataInitializer(StudentRepository studentRepository,
                           TeacherRepository teacherRepository,
                           SubjectRepository subjectRepository,
                           EnrollmentRepository enrollmentRepository,
                           AssignmentRepository assignmentRepository,
                           AppUserRepository appUserRepository,
                           PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assignmentRepository = assignmentRepository;
        this.appUserRepository = appUserRepository;
        this.encoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            studentRepository.save(new Student("Nguyễn Văn An", "an@hus.edu.vn", LocalDate.of(2004, 3, 12)));
            studentRepository.save(new Student("Trần Thị Bình", "binh@hus.edu.vn",  LocalDate.of(2003, 9, 1)));
            studentRepository.save(new Student("Lê Hoàng Cường", "cuong@hus.edu.vn", LocalDate.of(2004, 12, 25)));
        }

        if (teacherRepository.count() == 0) {
            teacherRepository.save(new Teacher("TS. Phạm Minh Đức", "duc@hus.edu.vn","Toán - Cơ - Tin học"));
            teacherRepository.save(new Teacher("PGS. Vũ Thị Hoa", "hoa@hus.edu.vn", "Vật lý"));
        }

        if (subjectRepository.count() == 0) {
            subjectRepository.save(new Subject("Giải tích 1", 4));
            subjectRepository.save(new Subject("Đại số tuyến tính", 3));
            subjectRepository.save(new Subject("Lập trình hướng đối tượng", 5));
        }

        // Bảng nối: chỉ seed khi đã có SV/GV/Môn và bảng nối còn trống
        List<Student> students = studentRepository.findAll();
        List<Teacher> teachers = teacherRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();

        if (enrollmentRepository.count() == 0 && !students.isEmpty() && subjects.size() >= 2) {
            // (SV0 - Môn0 - HK1 - 8.5), (SV0 - Môn1 - HK1 - 7.0), (SV1 - Môn0 - HK1 - 9.0)
            Enrollment e1 = new Enrollment(students.getFirst(), subjects.get(0), "2024-1"); e1.setGrade(8.5);
            Enrollment e2 = new Enrollment(students.getFirst(), subjects.get(1), "2024-1"); e2.setGrade(7.0);
            Enrollment e3 = new Enrollment(students.getFirst(), subjects.get(0), "2024-1"); e3.setGrade(9.0);
            enrollmentRepository.save(e1);
            enrollmentRepository.save(e2);
            enrollmentRepository.save(e3);
        }

        if (assignmentRepository.count() == 0 && !teachers.isEmpty() && subjects.size() >= 2) {
            // GV0 dạy Môn0 và Môn1, GV1 dạy Môn2
            assignmentRepository.save(new Assignment(teachers.get(0), subjects.get(0)));
            assignmentRepository.save(new Assignment(teachers.get(0), subjects.get(1)));
            if (teachers.size() >= 2 && subjects.size() >= 3) {
                assignmentRepository.save(new Assignment(teachers.get(1), subjects.get(2)));
            }
        }

        if (appUserRepository.count() == 0) {
            appUserRepository.save(new AppUser("admin@hus.edu.vn", encoder.encode("admin123"), Role.ADMIN)); // admin mặc định


            //Lưu các giáo viên và sinh viên với tk mặc định là email và mk mặc định
            for (Teacher t : teachers) {
                AppUser u = new AppUser(t.getEmail(), encoder.encode("123456"), Role.TEACHER);
                u.setPerson(t);
                appUserRepository.save(u);
            }
            for (Student s : students) {
                AppUser u = new AppUser(s.getEmail(), encoder.encode("123456"), Role.STUDENT);
                u.setPerson(s);
                appUserRepository.save(u);
            }
        }
    }
}
