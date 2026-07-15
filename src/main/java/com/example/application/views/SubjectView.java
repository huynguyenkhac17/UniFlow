package com.example.application.views;

import com.example.entity.Subject;
import com.example.service.SubjectService;
import com.example.service.EnrollmentService;
import com.example.service.AssignmentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "subject", layout = MainLayout.class)
@PageTitle("Subject View | UniFlow")
@RolesAllowed("ADMIN")
public class SubjectView extends VerticalLayout {

    private final SubjectService subjectService;
    private final EnrollmentService enrollmentService;
    private final AssignmentService assignmentService;

    private final Grid<Subject> grid = new Grid<>(Subject.class, false);
    private final TextField filter = new TextField();

    // Subject chỉ có tên + số tín chỉ (Integer) -> dùng IntegerField
    private final TextField name = new TextField("Tên học phần");
    private final IntegerField subjectId = new IntegerField("Mã học phần");
    private final IntegerField credit = new IntegerField("Số tín chỉ");
    private final Button save = new Button("Lưu");
    private final Button delete = new Button("Xóa");
    private final Button cancel = new Button("Hủy");
    private final Div form = new Div();

    private final Binder<Subject> binder = new Binder<>(Subject.class);
    private Subject editing;

    public SubjectView(SubjectService subjectService,
                       EnrollmentService enrollmentService,
                       AssignmentService assignmentService) {
        this.subjectService = subjectService;
        this.enrollmentService = enrollmentService;
        this.assignmentService = assignmentService;
        addClassName("subject-view");
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
        grid.addColumn(Subject::getName).setHeader("Tên môn").setAutoWidth(true);
        grid.addColumn(Subject::getCredit).setHeader("Số tín chỉ").setAutoWidth(true);
        grid.addColumn(Subject::getId).setHeader("Id").setAutoWidth(true);
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> editSubject(e.getValue()));
    }

    private void configureForm() {
        binder.forField(name).asRequired("Tên môn không được trống!")
                .bind(Subject::getName, Subject::setName);
        binder.forField(credit).asRequired("Số tín chỉ không được trống!")
                .bind(Subject::getCredit, Subject::setCredit);

        save.addClickListener(e -> saveSubject());
        delete.addClickListener(e -> deleteSubject());
        cancel.addClickListener(e -> closeEditor());

        FormLayout fields = new FormLayout(name, credit);
        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        form.add(fields, actions);
        form.addClassName("editor");
    }

    private HorizontalLayout getToolbar() {
        filter.setPlaceholder("Tìm theo tên...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        Button add = new Button("Thêm môn học");
        add.addClickListener(e -> { grid.asSingleSelect().clear(); editSubject(new Subject()); });

        HorizontalLayout toolbar = new HorizontalLayout(filter, add);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(subjectService.findByName(filter.getValue()));
    }

    private void editSubject(Subject subject) {
        if (subject == null) {
            closeEditor();
        } else {
            editing = subject;
            binder.readBean(subject);
            form.setVisible(true);
        }
    }

    private void saveSubject() {
        try {
            binder.writeBean(editing);
            subjectService.save(editing);
            updateList();
            closeEditor();
            Notification.show("Đã lưu!");
        } catch (ValidationException e) {
            Notification.show("Dữ liệu không hợp lệ");
        }
    }

    private void deleteSubject() {
        if (editing != null && editing.getId() != null) {
            // Môn bị tham chiếu bởi cả Enrollment lẫn Assignment -> chặn xóa
            if (enrollmentService.isSubjectEnrolled(editing.getId())) {
                Notification.show("Không thể xóa: môn đang có sinh viên đăng ký học.");
                return;
            }
            if (assignmentService.isSubjectAssigned(editing.getId())) {
                Notification.show("Không thể xóa: môn đang được phân công dạy.");
                return;
            }

            ConfirmDialog confirm = new ConfirmDialog();
            confirm.setHeader("Xóa môn học?");
            confirm.setText("Xóa \"" + editing.getName() + "\"? Không thể hoàn tác." );
            confirm.setCancelable(true);
            confirm.setCancelText("Thôi");
            confirm.setConfirmText("Xóa");
            confirm.setConfirmButtonTheme("error primary");
            confirm.addConfirmListener(e -> {
                subjectService.delete(editing);
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
