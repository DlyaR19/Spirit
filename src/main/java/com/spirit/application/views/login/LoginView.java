package com.spirit.application.views.login;

import com.spirit.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login")
@Route(value = "login", layout = MainView.class)
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        // Zentrieren des Inhalts
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f9fafb");

        // Login-Box erstellen
        Div loginBox = new Div();
        loginBox.getStyle()
                .set("padding", "2rem")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.1)")
                .set("background-color", "white")
                .set("max-width", "400px")
                .set("width", "100%");

        // Titel
        H1 title = new H1("Anmelden");
        title.getStyle()
                .set("font-size", "24px")
                .set("font-weight", "bold")
                .set("text-align", "center")
                .set("margin-bottom", "1rem");
        loginBox.add(title);

        // Benutzername-Feld
        TextField usernameField = new TextField("Benutzername");
        usernameField.setPlaceholder("Geben Sie Ihren Benutzernamen ein");
        usernameField.setWidthFull();
        usernameField.getStyle().set("margin-bottom", "1rem");
        loginBox.add(usernameField);

        // Passwort-Feld
        PasswordField passwordField = new PasswordField("Passwort");
        passwordField.setPlaceholder("Geben Sie Ihr Passwort ein");
        passwordField.setWidthFull();
        passwordField.getStyle().set("margin-bottom", "1rem");
        loginBox.add(passwordField);

        // Login-Button
        Button loginButton = new Button("Anmelden");
        loginButton.setWidthFull();
        loginButton.getStyle()
                .set("background-color", "#007bff")
                .set("color", "white")
                .set("padding", "0.8rem")
                .set("font-size", "16px")
                .set("border-radius", "8px")
                .set("cursor", "pointer");
        loginButton.addClickListener(event -> {
            // Login-Logik hier hinzufügen
        });
        loginBox.add(loginButton);

        // Passwort vergessen
        Anchor forgotPassword = new Anchor("#", "Passwort vergessen?");
        forgotPassword.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("margin-top", "1rem")
                .set("color", "#007bff")
                .set("font-size", "14px");
        loginBox.add(forgotPassword);

        // Registrierung
        Paragraph registerPrompt = new Paragraph("Sie haben noch kein Konto?");
        registerPrompt.getStyle()
                .set("margin-top", "1rem")
                .set("font-size", "14px")
                .set("text-align", "center");
        Anchor registerLink = new Anchor("register", "Melden Sie sich hier an!");
        registerLink.getStyle()
                .set("font-size", "14px")
                .set("color", "#007bff")
                .set("text-decoration", "underline");

        Div registerDiv = new Div(registerPrompt, registerLink);
        registerDiv.getStyle().set("text-align", "center");
        loginBox.add(registerDiv);

        // Footer
        Paragraph footer = new Paragraph("© 2024 Spirit. Alle Rechte vorbehalten.");
        footer.getStyle()
                .set("font-size", "12px")
                .set("color", "#888")
                .set("text-align", "center")
                .set("margin-top", "2rem");
        loginBox.add(footer);

        // Login-Box hinzufügen
        add(loginBox);
    }
}