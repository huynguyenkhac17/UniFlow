package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.example.dto.AssignmentDto;
import com.example.entity.AssignmentId;
import com.example.entity.Subject;
import com.example.entity.Teacher;
import com.example.service.AssignmentService;
import com.example.service.SubjectService;
import com.example.service.TeacherService;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "assignment", layout = MainLayout.class)
@PageTitle("Assignment | UniFlow")
@RolesAllowed("ADMIN")
public class AssignmentView extends VerticalLayout {

    private final AssignmentService assignmentService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;

    private final Grid<AssignmentDto> grid = new Grid<>(AssignmentDto.class, false);
    private final ComboBox<Teacher> teacher = new ComboBox<>("Giảng viên");
    private final ComboBox<Subject> subject = new ComboBox<>("Môn học");
    private final Button save = new Button("Lưu");
    private final Button delete = new Button("Xóa");
    private final Button cancel = new Button("Hủy");
    private final Div form = new Div();

    private List<Teacher> allTeachers;
    private List<Subject> allSubjects;
    private AssignmentDto editing;   // null = đang thêm mới

    public AssignmentView(AssignmentService assignmentService, TeacherService teacherService, SubjectService subjectService) {
        this.assignmentService = assignmentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        addClassName("assignment-view");
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
        grid.addColumn(AssignmentDto::getTeacherName).setHeader("Giảng viên").setAutoWidth(true);
        grid.addColumn(AssignmentDto::getSubjectName).setHeader("Môn học").setAutoWidth(true);
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> editAssignment(e.getValue()));
    }

    private void configureForm() {
        allTeachers = teacherService.findAll();
        allSubjects = subjectService.findAll();

        teacher.setItems(allTeachers);
        teacher.setItemLabelGenerator(Teacher::getName);   // hiện tên thay vì toString
        subject.setItems(allSubjects);
        subject.setItemLabelGenerator(Subject::getName);

        save.addClickListener(e -> saveAssignment());
        delete.addClickListener(e -> deleteAssignment());
        cancel.addClickListener(e -> closeEditor());

        form.add(new FormLayout(teacher, subject), new HorizontalLayout(save, delete, cancel));
        form.addClassName("editor");
    }

    private HorizontalLayout getToolbar() {
        Button add = new Button("Phân công");
        add.addClickListener(e -> { grid.asSingleSelect().clear(); addAssignment(); });
        HorizontalLayout toolbar = new HorizontalLayout(add);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(assignmentService.findAllDto());
    }

    private void addAssignment() {
        editing = null;
        teacher.clear(); subject.clear();
        setKeyReadOnly(false);           // thêm mới -> chọn được cả 2
        save.setVisible(true);
        form.setVisible(true);
    }

    private void editAssignment(AssignmentDto dto) {
        if (dto == null) {
            closeEditor();
            return;
        }
        editing = dto;
        teacher.setValue(findTeacher(dto.getTeacherId()));  // đúng instance trong list
        subject.setValue(findSubject(dto.getSubjectId()));
        // Assignment chỉ có khóa (teacher, subject), không có field nào khác để sửa
        // -> chọn dòng có sẵn thì chỉ cho Xóa, khóa 2 ComboBox và ẩn nút Lưu
        setKeyReadOnly(true);
        save.setVisible(false);
        form.setVisible(true);
    }

    private void setKeyReadOnly(boolean ro) {
        teacher.setReadOnly(ro);
        subject.setReadOnly(ro);
    }

    private void saveAssignment() {
        Teacher t = teacher.getValue();
        Subject sub = subject.getValue();
        if (t == null || sub == null) {
            Notification.show("Chọn giảng viên và môn học");
            return;
        }
        assignmentService.save(t, sub);
        updateList(); closeEditor();
        Notification.show("Đã phân công");
    }

    private void deleteAssignment() {
        if (editing != null) {
            assignmentService.delete(new AssignmentId(editing.getTeacherId(), editing.getSubjectId()));
            updateList(); closeEditor();
            Notification.show("Đã xóa");
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        editing = null;
        grid.asSingleSelect().clear();
    }

    private Teacher findTeacher(Long id) {
        return allTeachers.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    private Subject findSubject(Long id) {
        return allSubjects.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }
}
