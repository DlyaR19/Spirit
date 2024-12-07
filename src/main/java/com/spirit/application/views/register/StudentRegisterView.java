package com.spirit.application.views.register;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StudentRegisterView extends BaseRegisterView {

    private TextField firstNameTextField;
    private TextField lastNameTextField;

    @Autowired
    public StudentRegisterView(@Qualifier("registerProxy") RegisterInterface registerInterface) {
        super(registerInterface);
        setupStudentForm();
    }
    // TODO andere Felder hinzufügen wie z.B. Studiengang, Geburtsdatum (date picker), etc.
    private void setupStudentForm() {
        firstNameTextField = new TextField("Vornamen");
        lastNameTextField = new TextField("Nachnamen");

        FormLayout nameLayout = new FormLayout();
        nameLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 2)
        );
        nameLayout.add(firstNameTextField, lastNameTextField);
        nameLayout.setColspan(firstNameTextField, 1);
        nameLayout.setColspan(lastNameTextField, 1);

        FormLayout passwordLayout = new FormLayout();
        passwordLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 2)
        );
        passwordLayout.add(passwordField, passwordConfirmationField);
        passwordLayout.setColspan(passwordField, 1);
        passwordLayout.setColspan(passwordConfirmationField, 1);

        add(nameLayout, usernameField, emailField, passwordLayout, submitButton, cancelButton);
    }

    @Override
    protected void register() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        String firstName = this.firstNameTextField.getValue();
        String lastName = this.lastNameTextField.getValue();
        String passwordConfirmation = passwordConfirmationField.getValue();

        registerInterface.registerStudent(username, password, email, firstName, lastName, passwordConfirmation);
        UI.getCurrent().navigate(Globals.Pages.LOGIN);
    }
}