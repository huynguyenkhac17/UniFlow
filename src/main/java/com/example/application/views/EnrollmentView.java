package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.dto.EnrollmentDto;
import com.example.entity.Student;
import com.example.entity.Subject;
import com.example.service.EnrollmentService;
import com.example.service.StudentService;
import com.example.service.SubjectService;
import com.example.entity.EnrollmentId;
import jakarta.annotation.security.RolesAllowed;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;


@Route(value = "enrollment", layout = MainLayout.class)
@PageTitle("Enrollment | UniFlow")
@RolesAllowed("ADMIN")
public class EnrollmentView extends VerticalLayout {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    private final Grid<EnrollmentDto> grid = new Grid<>(EnrollmentDto.class, false);
    private final ComboBox<Student> student = new ComboBox<>("Sinh viên");
    private final ComboBox<Subject> subject = new ComboBox<>("Môn học");
    private final ComboBox<String> semester = new ComboBox<>("Học kỳ");
    private final NumberField grade = new NumberField("Điểm");
    private final Button save = new Button("Lưu");
    private final Button delete = new Button("Xóa");
    private final Button cancel = new Button("Hủy");
    private final Div form = new Div();

    private List<Student> allStudents;
    private List<Subject> allSubjects;
    private EnrollmentDto editing;   // null = đang thêm mới

    public EnrollmentView(EnrollmentService enrollmentService, StudentService studentService, SubjectService subjectService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        addClassName("enrollment-view");
        setSizeFull();

        configureGrid();
        configureForm();

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addColumn(EnrollmentDto::getStudentName).setHeader("Sinh viên").setAutoWidth(true);
        grid.addColumn(EnrollmentDto::getSubjectName).setHeader("Môn học").setAutoWidth(true);
        grid.addColumn(EnrollmentDto::getSemester).setHeader("Học kỳ").setAutoWidth(true);
        grid.addColumn(EnrollmentDto::getGrade).setHeader("Điểm").setAutoWidth(true);
        grid.setSizeFull();

        grid.setPartNameGenerator(dto -> (dto.getGrade() != null && dto.getGrade() < 4) ? "fail" : null); // điều kiện để tô fail điểm

        grid.asSingleSelect().addValueChangeListener(e -> editEnrollment(e.getValue()));
    }

    private void configureForm() {
        allStudents = studentService.findAll();
        allSubjects = subjectService.findAll();

        student.setItems(allStudents);
        student.setItemLabelGenerator(Student::getName);  // hiện tên thay vì toString mặc định
        student.setPlaceholder("Chọn sinh viên");


        subject.setItems(allSubjects);
        subject.setItemLabelGenerator(Subject::getName);
        subject.setPlaceholder("Chọn môn học");

        semester.setItems(semesterOptions());
        semester.setPlaceholder("Chọn học kỳ");

        grade.setMin(0); grade.setMax(10); grade.setStep(0.1);

        save.addClickListener(e -> saveEnrollment());
        delete.addClickListener(e -> deleteEnrollment());
        cancel.addClickListener(e -> closeEditor());

        form.add(new FormLayout(student, subject, semester, grade), new HorizontalLayout(save, delete, cancel));
        form.addClassName("editor");
    }

    private HorizontalLayout getToolbar() {
        Button add = new Button("Đăng kí");
        add.addClickListener(e -> {
            grid.asSingleSelect().clear();
            addEnrollment();
        });
        HorizontalLayout toolbar = new HorizontalLayout(add);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(enrollmentService.findAllDto());
    }

    private void addEnrollment() {
        editing = null;
        student.clear();
        subject.clear();
        semester.setItems(semesterOptions());   // reset về danh sách chuẩn
        semester.clear();
        grade.clear();
        setKeyReadOnly(false);           // thêm mới -> chọn được khóa
        form.setVisible(true);
    }

    private void editEnrollment(EnrollmentDto dto) {
        if (dto == null) {
            closeEditor();
            return;
        }

        editing = dto;
        student.setValue(findStudent(dto.getStudentId()));  // đúng instance trong list
        subject.setValue(findSubject(dto.getSubjectId()));
        // dữ liệu cũ có thể sai format -> thêm tạm vào danh sách để vẫn hiển thị được
        String sem = dto.getSemester();
        List<String> opts = new ArrayList<>(semesterOptions());
        if (sem != null && !opts.contains(sem)) opts.add(sem);
        semester.setItems(opts);
        semester.setValue(sem);
        grade.setValue(dto.getGrade());
        setKeyReadOnly(true);
        form.setVisible(true);
    }

    // Không cho sửa khóa chính
    private void setKeyReadOnly(boolean ro) {
        student.setReadOnly(ro);
        subject.setReadOnly(ro);
        semester.setReadOnly(ro);
    }

    private void saveEnrollment() {
        Student s = student.getValue();
        Subject sub = subject.getValue();
        String sem = semester.getValue();
        Double g = grade.getValue();
        if (s == null
                || sub == null
                || sem == null || sem.isBlank()
        ) {
            Notification.show("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        if (g != null && (g < 0 || g > 10)) {
            Notification.show("Điểm phải từ 0 đến 10!");
            return;
        }

        if (g != null && Math.abs(g * 10 - Math.round(g * 10)) > 1e-9) {
            Notification.show("Điểm chỉ được có 1 chữ số thập phân.");
            return;
        }

        enrollmentService.save(s, sub, sem, g);

        updateList();
        closeEditor();
        Notification.show("Đã lưu đăng kí!");
    }

    private void deleteEnrollment() {
        if (editing != null) {
            enrollmentService.delete(new EnrollmentId(editing.getStudentId(), editing.getSubjectId(), editing.getSemester()));
            updateList(); closeEditor();
            Notification.show("Đã xóa");
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        editing = null;
        grid.asSingleSelect().clear();
    }

    // Sinh danh sách học kỳ: HK1/HK2 cho mỗi năm học từ 2020 tới năm hiện tại + 1
    private List<String> semesterOptions() {
        int currentYear = Year.now().getValue();
        List<String> options = new ArrayList<>();
        for (int year = 2020; year <= currentYear + 1; year++) {
            for (int ky = 1; ky <= 2; ky++) {
                options.add("HK" + ky + " " + year + "-" + (year + 1));
            }
        }
        return options;
    }

    // tìm trong list trên ram (ko cần truy vấn db)
    private Student findStudent(Long id) {
        return allStudents.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    private Subject findSubject(Long id) {
        return allSubjects.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }
}