package com.spirit.application.views.login;


import com.spirit.application.dto.UserDTO;
import com.spirit.application.service.LoginService;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@PageTitle("Login")
@Route(Globals.Pages.LOGIN)
//@CssImport("./styles/index.css")
@Menu(order = 1)
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm;
    private final transient LoginService loginService;

    public LoginView(LoginService loginService) {
        loginForm = setUpUI();
        //loginForm.addForgotPasswordListener(e -> UI.getCurrent().navigate(Globals.Pages.FORGOT_PASSWORD));
        loginForm.addLoginListener(this::handleLogin);
        this.loginService = loginService;
    }

    private LoginForm setUpUI() {
        LoginForm setUpLoginForm;
        addClassName("main");
        setSizeFull();

        setUpLoginForm = createLoginForm();
        setUpLoginForm.setAction("login");

        add(setUpLoginForm);
        this.setAlignItems(Alignment.CENTER);

        HorizontalLayout additionalInfoLayout = new HorizontalLayout();
        additionalInfoLayout.add(new Text("Sie haben noch kein Konto? "), new Anchor(Globals.Pages.SIGNUP, "Melden Sie sich hier an!"));
        additionalInfoLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setAlignItems(Alignment.CENTER);
        footer.setSpacing(true);
        footer.add(new Text("© 2024 Spirit"), new Anchor(Globals.Pages.ABOUTUS, "About Spirit"));

        VerticalLayout layout = new VerticalLayout(setUpLoginForm, additionalInfoLayout, footer);
        layout.setAlignItems(Alignment.CENTER);

        add(layout);

        return setUpLoginForm;
    }

    private LoginForm createLoginForm() {
        LoginForm component = new LoginForm();
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();

        i18nForm.setTitle("Anmelden");
        i18nForm.setUsername("Benutzername");
        i18nForm.setPassword("Passwort");
        i18nForm.setSubmit("Anmelden");
        i18nForm.setForgotPassword("Passwort vergessen");
        i18n.setForm(i18nForm);
        component.setI18n(i18n);

        return component;
    }

    private void handleLogin(AbstractLogin.LoginEvent input) {
        try {
            loginService.startSession(new UserDTO(loginService.login(input.getUsername(), input.getPassword())));
        } catch (Exception e) {
            Notification.show("Benutzer mit diesem Benutzernamen und/oder Passwort konnte nicht gefunden werden!");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
            Notification notification = Notification.show("Anmelden fehlgeschlagen");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
