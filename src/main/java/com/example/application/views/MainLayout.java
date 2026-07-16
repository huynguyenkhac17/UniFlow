package com.example.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;


@PermitAll // cho mọi user đã đăng nhập
public class MainLayout extends AppLayout { // Cho 3 vùng: thanh ngang NavBar bên trên, drawer bên trái và phần content rộng nhất bên phải
    private final AccessAnnotationChecker accessChecker;

    // Mỗi role chỉ nhìn thấy view của mình
    private void addLinkIfAllowed(HasComponents nav, String text, Class<? extends Component> view) {
        if (accessChecker.hasAccess(view)) {  // kiểm tra trong annotaion RolesAllowed của class vieww có quyền ko? lấy từ security context trong threadlocal của request hiện tại.
            RouterLink link = new RouterLink(text, view);
            link.addClassName("nav-link");
            nav.add(link);
        }
    }

    // Được vaadin tạo mới mỗi lần điều hướng
    public MainLayout(AccessAnnotationChecker accessChecker, AuthenticationContext authContext) { // Kiểm tra quyền trước khi tạo routerlink
        this.accessChecker = accessChecker;

        Span uni = new Span("Uni");
        Span flow = new Span("Flow");
        flow.addClassName("logo-accent");

        H1 logo = new H1(uni, flow);
        logo.addClassName("app-logo");

        DrawerToggle drawerToggle = new DrawerToggle();

        String name = authContext.getPrincipalName().orElse("?"); // lấy username từ security
        String role = String.join(", ", authContext.getGrantedRoles());

        Span userInfo = new Span(name + " * " + role);
        userInfo.addClassName("user-info");

        Button logout = new Button("Đăng xuất", VaadinIcon.SIGN_OUT.create());
        logout.addClassName("app-logout");
        logout.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_ERROR);
        logout.addClickListener(e -> authContext.logout()); // xóa session trong context hiện tại

        HorizontalLayout userBox = new HorizontalLayout(userInfo, logout);
        userBox.setAlignItems(FlexComponent.Alignment.CENTER);
        userBox.addClassName("user-box");
        userBox.setPadding(true);

        HorizontalLayout navBar = new HorizontalLayout();

        navBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navBar.setWidthFull();
        navBar.add(drawerToggle, logo, userBox);
        navBar.expand(logo);

        addToNavbar(navBar);


        VerticalLayout nav = new VerticalLayout();
        nav.setWidthFull();
        nav.setPadding(false); // khoảng cách giữa elemnt và viền
        nav.setSpacing(false); // khoảng cách giữa các elemen
        nav.addClassName("nav-drawer");

        addLinkIfAllowed(nav, "Tổng quan", DashboardView.class);
        addLinkIfAllowed(nav, "Sinh viên", StudentView.class);
        addLinkIfAllowed(nav, "Giảng viên", TeacherView.class);
        addLinkIfAllowed(nav, "Môn học", SubjectView.class);
        addLinkIfAllowed(nav, "Đăng kí học", EnrollmentView.class);
        addLinkIfAllowed(nav, "Phân công", AssignmentView.class);

        addToDrawer(nav); // thêm vào phần điều hướng (bên trái)

        //<theme-editor-local-classname>
        addClassName("main-layout-app-layout");
    }
}
