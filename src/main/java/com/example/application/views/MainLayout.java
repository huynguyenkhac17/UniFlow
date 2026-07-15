package com.example.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import jakarta.annotation.security.PermitAll;


@PermitAll // cho mọi user đã đăng nhập
public class MainLayout extends AppLayout { // Cho 2 vùng NavBar và Drawer

    public MainLayout() {
        H1 logo = new H1("UniFlow");
        logo.addClassName("app-logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo); // Main Direction ngang, cross dọc
        //<theme-editor-local-classname>
        header.addClassName("main-layout-horizontal-layout-1");
                                // Nút hình 3 gạch để mở tab bên + Logo
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); // Căn giữa theo chiều dọc
        header.setWidthFull(); // Chiều ngang full
        header.addClassName("app-header");

        H3 role = new H3("admin");

        HorizontalLayout roleLayout = new HorizontalLayout(role); // đặt H3 vào trong layout để style
        roleLayout.addClassName("app-role-layout");
        roleLayout.setPadding(true);
        roleLayout.getStyle()
                .set("padding", "4px var(--lumo-space-s)")
                .set("border", "2px solid var(--lumo-contrast-20pct)")
                .set("margin", "4px var(--lumo-space-s)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("background", "var(--lumo-base-color)");

        addToNavbar(header, roleLayout); // Thêm header vào thanh điều hướng


        RouterLink dashboard = new RouterLink("Tổng quan", DashboardView.class);
        dashboard.addClassName("nav-link");

        RouterLink students = new RouterLink("Sinh viên", StudentView.class); // link đến studentview
        students.addClassName("nav-link");

        RouterLink teachers = new RouterLink("Giảng viên", TeacherView.class);
        teachers.addClassName("nav-link");

        RouterLink subjects = new RouterLink("Môn học",  SubjectView.class);
        subjects.addClassName("nav-link");

        RouterLink enrollments = new RouterLink("Đăng ký học", EnrollmentView.class);
        enrollments.addClassName("nav-link");

        RouterLink assignments = new RouterLink("Phân công dạy", AssignmentView.class);
        assignments.addClassName("nav-link");

        VerticalLayout nav = new VerticalLayout(dashboard, students, teachers, subjects, enrollments, assignments);
        nav.setWidthFull();
        nav.setPadding(false);   // bỏ padding mặc định để link trải sát mép
        nav.setSpacing(false);   // bỏ khoảng cách để các link thành dải liền
        nav.addClassName("nav-drawer");
        addToDrawer(nav);
        //<theme-editor-local-classname>
        addClassName("main-layout-app-layout");
    }
}
