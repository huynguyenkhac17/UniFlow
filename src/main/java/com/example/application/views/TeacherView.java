package com.example.application.views;

import com.example.entity.Teacher;
import com.example.service.AssignmentService;
import com.example.service.TeacherService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "teacher", layout = MainLayout.class)
@PageTitle("Teacher View | UniFlow")
@RolesAllowed({"ADMIN"})
public class TeacherView extends VerticalLayout {

    private final TeacherService teacherService;
    private final AssignmentService assignmentService;

    // reuseble components
    private final Grid<Teacher> grid = new Grid<>(Teacher.class, false);
    private final TextField filter = new TextField();

    // Các ô trong form
    private final TextField name = new TextField("Tên");
    private final EmailField mail = new EmailField("Email");
    private final PasswordField password = new PasswordField("Password");
    private final TextField department = new TextField("Khoa");
    private final Button save = new Button("Lưu");
    private final Button delete = new Button("Xóa");
    private final Button cancel = new Button("Hủy");
    private final Div form = new Div();

    private final Binder<Teacher> binder = new Binder<>(Teacher.class);
    private Teacher editing;

    public TeacherView(TeacherService teacherService, AssignmentService assignmentService) {
        this.teacherService = teacherService;
        this.assignmentService = assignmentService;
        addClassName("teacher-view");
        setSizeFull();

        configureGrid();
        configureForm();

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid); // grid gấp dôi form
        content.setFlexGrow(1, form);
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addColumn(Teacher::getName).setHeader("Tên").setAutoWidth(true);
        grid.addColumn(Teacher::getEmail).setHeader("Mail").setAutoWidth(true);
        grid.addColumn(Teacher::getDepartment).setHeader("Khoa").setAutoWidth(true);
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e -> editTeacher(e.getValue()));
    }

    private void configureForm() {
        binder.forField(name).asRequired("Tên không được trống!").bind(Teacher::getName, Teacher::setName);
        binder.forField(mail).asRequired("Email không được trống!").withValidator(new EmailValidator("Mail không hợp lệ!")).bind(Teacher::getEmail, Teacher::setEmail);
        binder.forField(department).bind(Teacher::getDepartment, Teacher::setDepartment);
        binder.forField(password).bind(Teacher::getPassword, Teacher::setPassword);

        save.addClickListener(e -> saveTeacher());
        delete.addClickListener(e -> deleteTeacher());
        cancel.addClickListener(e -> closeEditor());

        FormLayout fields = new FormLayout(name, mail, password, department);
        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        form.add(fields, actions);
        form.addClassName("editor");
    }

    private HorizontalLayout getToolbar() {
        filter.setPlaceholder("Tìm theo tên...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        Button add = new Button("Thêm giảng viên");
        add.addClickListener(e -> { grid.asSingleSelect().clear(); editTeacher(new Teacher()); });

        HorizontalLayout toolbar = new HorizontalLayout(filter, add);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(teacherService.findByName(filter.getValue())); // filter ko có thì là findALl()
    }

    private void editTeacher(Teacher teacher) {
        if (teacher == null) {
            closeEditor();
        } else {
            editing = teacher;
            binder.readBean(teacher);
            form.setVisible(true);
        }
    }

    private void saveTeacher() {
        try {
            binder.writeBean(editing);
            teacherService.save(editing);
            updateList();
            closeEditor();
            Notification.show("Đã lưu!");
        } catch (ValidationException e) {
            Notification.show("Dữ liệu không hợp lệ");
        }
    }

    private void deleteTeacher() {
        if (editing != null && editing.getId() != null) {
            // kiểm tra giảng viên còn môn học ko trc khi xóa
            if (assignmentService.isTeacherAssigned(editing.getId())) {
                Notification.show("Không thể xóa: giảng viên đang có phân công dạy.");
                return;
            }
            
            ConfirmDialog confirm = new ConfirmDialog();
            confirm.setHeader("Xóa giảng viên?");
            confirm.setText("Xóa \"" + editing.getName() + "\"? Không thể hoàn tác." );
            confirm.setCancelable(true);
            confirm.setCancelText("Thôi");
            confirm.setConfirmText("Xóa");
            confirm.setConfirmButtonTheme("error primary");
            confirm.addConfirmListener(e -> {
                teacherService.delete(editing);
                updateList();
                closeEditor();
                Notification.show("ĐÃ xóa!");
            });
            confirm.open();
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        editing = null;
        grid.asSingleSelect().clear();
    }
}
