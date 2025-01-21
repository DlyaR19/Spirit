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

/**
 * The view for the login page, providing a user interface for user authentication.
 * This view contains a login form, links to register a new account, and an "About Spirit" footer.
 * It also handles the login process and session management.
 */
@CssImport("./themes/spirit/views/LoginView.css")
@PageTitle("Login")
@Route(Globals.Pages.LOGIN)
@Menu(order = 1)
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm;
    private final transient LoginService loginService;

    /**
     * Constructor for initializing the login view with a login form and necessary layout.
     * It sets up the login form, layout, and additional components such as registration link and footer.
     * @param loginService The service to handle user login and session management.
     */
    public LoginView(LoginService loginService) {
        this.loginService = loginService;

        // Login-Formular
        loginForm = createLoginForm();
        loginForm.setAction("login");
        loginForm.addLoginListener(this::handleLogin);
        loginForm.addForgotPasswordListener(event -> {
           Notification.show("Noch nicht implementiert!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_WARNING);
        });

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

    /**
     * Creates the login form with localized text for form fields and actions.
     * @return The login form component.
     */
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

    /**
     * Creates an anchor (hyperlink) element for navigation.
     * @param href The URL to link to.
     * @param text The text to display for the link.
     * @return The configured anchor element.
     */
    private Anchor createAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);
        anchor.getStyle()
                .set("color", "#0056b3")
                .set("text-decoration", "none")
                .set("font-weight", "bold");
        anchor.addClassName("link");
        return anchor;
    }

    /**
     * Handles the login event triggered by the login form submission.
     * Attempts to log the user in using the provided username and password. If successful, starts a new session.
     * If the login fails, shows an error notification.
     * @param input The login event containing the entered username and password.
     */
    private void handleLogin(AbstractLogin.LoginEvent input) {
        try {
            loginService.startSession(new UserDTO(loginService.login(input.getUsername(), input.getPassword())));
        } catch (Exception e) {
            Notification.show("Benutzername oder Passwort falsch", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Called before navigation to this view. Checks if there was a login error indicated by the query parameter "error".
     * If an error is present, shows a notification and sets the login form to error state.
     * @param event The before enter event.
     */
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