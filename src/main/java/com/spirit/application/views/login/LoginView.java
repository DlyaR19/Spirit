package com.spirit.application.views.login;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.service.LoginService;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@CssImport("./themes/spirit/views/LoginView.css")
@PageTitle("Login")
@Route(Globals.Pages.LOGIN)
@Menu(order = 1)
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm;
    private final transient LoginService loginService;

    public LoginView(LoginService loginService) {
        this.loginService = loginService;

        // Login-Formular
        loginForm = createLoginForm();
        loginForm.setAction("login");
        loginForm.addLoginListener(this::handleLogin);

        // Haupt-Container
        Div container = new Div();
        container.addClassName("login-container");

        // Layout für Formular-Inhalt
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.addClassName("form-layout");

        // Zusatzinfos
        VerticalLayout additionalInfoLayout = new VerticalLayout();
        additionalInfoLayout.setAlignItems(Alignment.CENTER);
        additionalInfoLayout.setSpacing(false);

        // Text für Registrierung
        HorizontalLayout registerInfo = new HorizontalLayout();
        registerInfo.setAlignItems(Alignment.CENTER);
        registerInfo.add(
                new Text("Sie haben noch kein Konto? "),
                createAnchor(Globals.Pages.SIGNUP, "Melden Sie sich hier an!")
        );

        // Footer mit Link "About Spirit"
        Div footer = new Div();
        footer.add(createAnchor(Globals.Pages.ABOUTUS, "About Spirit"));
        footer.addClassName("footer");

        // Hinzufügen zu den Zusatzinfos
        additionalInfoLayout.add(registerInfo, footer);

        // Zusammenstellen des Formularlayouts
        formLayout.add(loginForm, additionalInfoLayout);

        // Formular in Container einfügen
        container.add(formLayout);

        // Zentrieren
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Container hinzufügen
        add(container);
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

    private Anchor createAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);
        anchor.getStyle()
                .set("color", "#0056b3")
                .set("text-decoration", "none")
                .set("font-weight", "bold");
        anchor.addClassName("link");
        return anchor;
    }

    private void handleLogin(AbstractLogin.LoginEvent input) {
        try {
            loginService.startSession(new UserDTO(loginService.login(input.getUsername(), input.getPassword())));
        } catch (Exception e) {
            Notification.show("Benutzername oder Passwort falsch", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
            Notification notification = Notification.show("Anmelden fehlgeschlagen!");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}