package com.example.application.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Đăng nhập | UniFlow")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm(); // Username, password, login button, error mess

    public LoginView() {
        login.setAction("login");  // POST tới enpoint "/login" của VaadinSecurityConfigurer trong SpringSecurity
        add(login);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }


    // Hook trước khi render trang
    @Override
    public void beforeEnter(BeforeEnterEvent e) { // UI + Route + Location + NavigatinoTarget
        if (e.getLocation()                     // url hiện tại: path + querry
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);}
    }
}
