package com.example.application.views;

import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.service.EnrollmentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.text.Normalizer;

@Route(value = "student", layout = MainLayout.class) 
//@RouteAlias(value = "", layout = MainLayout.class) // vào luôn view này trong  route /
@PageTitle("Student View | UniFlow")
public class StudentView  extends VerticalLayout {
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    // reuseble components
    private final Grid<Student> grid = new Grid<>(Student.class, false); // tạo thủ công các cột);
    private final TextField filter = new TextField(); // 

    // Các ô trong form
    private final TextField name = new TextField("Tên");
    private final EmailField mail = new EmailField("Email");
    private final PasswordField password = new PasswordField("Password");
    private final DatePicker dob = new DatePicker("Ngày sinh");
    private final Button save = new Button("Lưu");
    private final Button delete = new Button("Xóa");
    private final Button cancel = new Button("Hủy");
    private final Div form = new Div();

    private final Binder<Student> binder = new Binder<>(Student.class);
    private ListDataProvider<Student> dataProvider; // Dùng list để sort

    // Dùng 1 biến editing để lưu trạng thái trước khi save
    private Student editing;

    public void editStudent(Student student) {
        if (student == null) closeEditor();
        else {
            editing = student;
            binder.readBean(student);
            form.setVisible(true);
        }
    }


    // ẩn form, và xóa dòng chọn hiện tại
    private void closeEditor() {
        form.setVisible(false);
        editing = null;
        grid.asSingleSelect().clear();
    }

    private void refreshData() {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(studentService.findAll());
        dataProvider.refreshAll();
    }

    private void deleteStudent() {
        if (editing != null && editing.getId() != null) {
            // Nếu sinh viên đã đăng kí môn thì ko xóa đc
            if (enrollmentService.isStudentEnrolled(editing.getId())) {
                Notification.show("Không thể xóa: sinh viên đã đăng ký học.");
                return;
            }

            ConfirmDialog confirm = new ConfirmDialog();
            confirm.setHeader("Xóa sinh viên?");
            confirm.setText("Xóa \"" + editing.getName() + "\"? Không thể hoàn tác." );
            confirm.setCancelable(true);
            confirm.setCancelText("Hủy");
            confirm.setConfirmText("Xóa");
            confirm.setConfirmButtonTheme("error primary");
            confirm.addConfirmListener(e -> {
                studentService.delete(editing);
                refreshData();
                closeEditor();
                Notification.show("ĐÃ xóa!");
            });
            confirm.open(); // chỉ open() để attack, addConfirmListener tự động close()
        }
    }

    private void saveStudent() {
        try {
            binder.writeBean(editing); // binder chỉ cập nhật object

            studentService.save(editing); // service mới lưu xuống db
            refreshData();
            closeEditor();
            Notification.show("Đã lưu!");
            
        } catch (ValidationException e) {
            Notification.show("Dữ liệu không hợp lệ: " + e);
        }
    }

    public static String normalize(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private void configureGrid() {
        // Thêm các cột vào bảng
        Grid.Column<Student> nameColumn = grid.addColumn(Student::getName)
                                                .setHeader("Tên").setAutoWidth(true)
                                                .setSortable(true)
                                                .setComparator(Student::getName);

        Grid.Column<Student> mailColumn = grid.addColumn(Student::getMail)
                                                .setHeader("Email").setAutoWidth(true);

        Grid.Column<Student> dodColumn = grid.addColumn(Student::getDob)
                                                .setHeader("Ngày sinh").setAutoWidth(true);
        grid.setSizeFull();

        dataProvider = new ListDataProvider<>(studentService.findByName(filter.getValue())); // ban đầu chưa nhập gì nên load hết db lần đầu
        grid.setItems(dataProvider);

        HeaderRow headerRow = grid.appendHeaderRow();
        TextField nameFilter = new TextField();

        nameFilter.setPlaceholder("Lọc tên..."); // Filter trên ListProvider (predicate) trong RAM
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);

        nameFilter.addValueChangeListener(e -> {
            String keyword = normalize(e.getValue().trim().toLowerCase());

            dataProvider.clearFilters();

            if (!keyword.isEmpty()) {
                dataProvider.addFilter(student ->
                        student.getName() != null &&
                                normalize(student.getName().toLowerCase()).contains(keyword));
            }
        });

        // chọn dòng nào coi như đang edit dòng đó
        grid.asSingleSelect().addValueChangeListener(e -> editStudent(e.getValue()));
        headerRow.getCell(nameColumn).setComponent(nameFilter);
    }

    private void configureForm() {
        // Nối mỗi ô với thuộc tính của Student + validate;
        binder.forField(name).asRequired("Tên không đuợc trống!").bind(Student::getName, Student::setName);
        binder.forField(mail).asRequired("Emali không được trống trống!").withValidator(new EmailValidator("Mail không hợp lệ!")).bind(Student::getMail, Student::setMail);
        binder.forField(dob).bind(Student::getDob, Student::setDob);
        binder.forField(password).bind(Student::getPassword, Student::setPassword);

        save.addClickListener(e -> saveStudent());
        delete.addClickListener(e -> deleteStudent());
        cancel.addClickListener(e -> closeEditor());

        FormLayout fields = new FormLayout(name, mail, dob, password);
        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        form.add(fields, actions);
    }

    private HorizontalLayout getToolbar() {
        filter.setPlaceholder("Lọc theo tên..."); // Truy vấn DB
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> {
            String keyword = filter.getValue();

            dataProvider.getItems().clear();
            dataProvider.getItems().addAll(studentService.findByName(filter.getValue()));
            dataProvider.refreshAll();
        });
        Button add = new Button("Thêm sinh viên");
        // Ấn thêm -> bỏ chọn dòng cũ + mở form
        add.addClickListener(e -> {grid.asSingleSelect().clear(); editStudent(new Student());});

        HorizontalLayout toolbar = new HorizontalLayout(filter, add);
        toolbar.setSpacing(true);
        toolbar.setClassName("toolbar");
        return toolbar;
    }


    public StudentView(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        addClassName("student-view");
        setSizeFull();

        configureGrid();
        configureForm();

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();

        add(getToolbar(), content);
        closeEditor();
    }
}
