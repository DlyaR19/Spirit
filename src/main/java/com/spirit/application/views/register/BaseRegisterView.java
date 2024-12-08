package com.spirit.application.views.register;

import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouterLayout;

public abstract class BaseRegisterView extends FormLayout implements RouterLayout {

    protected final transient RegisterInterface registerInterface;
    protected TextField usernameField;
    protected PasswordField passwordField;
    protected EmailField emailField;
    protected PasswordField passwordConfirmationField;
    protected Button submitButton;
    protected Button cancelButton;
    protected Span errorMessageField;
    protected VerticalLayout verticalLayout;

    protected BaseRegisterView(RegisterInterface registerInterface) {
        this.registerInterface = registerInterface;
        setupLayout();
        setupForm();
        addButtons();
    }

    private void setupLayout() {
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
        addClassName("registration-form");
    }

    private void setupForm() {
        usernameField = new TextField("Benutzername");
        passwordField = new PasswordField("Passwort");
        emailField = new EmailField("Email");
        passwordConfirmationField = new PasswordField("Passwort bestätigen");
        submitButton = new Button("Registrieren");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton = new Button("Abbrechen");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        errorMessageField = new Span();

        add(usernameField, 2);
        add(emailField, 2);
        add(passwordField, 1);
        add(passwordConfirmationField, 1);
    }

    private void addButtons() {
        submitButton.addClickListener(e -> register());
        cancelButton.addClickListener(e -> UI.getCurrent().navigate(Globals.Pages.LOGIN));
        add(errorMessageField);
        add(new HorizontalLayout(cancelButton, submitButton));
    }

    protected abstract void register();
}