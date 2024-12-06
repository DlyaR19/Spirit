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
public class UnternehmenRegisterView extends BaseRegisterView {

    private TextField companyNameField;

    @Autowired
    public UnternehmenRegisterView(@Qualifier("registerProxy") RegisterInterface registerInterface) {
        super(registerInterface);
        setupUnternehmenForm();
    }

    // TODO andere Felder hinzufügen wie z.B. Branche, Kontaktperson, etc.
    private void setupUnternehmenForm() {
        companyNameField = new TextField("Name des Unternehmens");

        FormLayout passwordLayout = new FormLayout();
        passwordLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 2)
        );
        passwordLayout.add(passwordField, passwordConfirmationField);
        passwordLayout.setColspan(passwordField, 1);
        passwordLayout.setColspan(passwordConfirmationField, 1);

        add(companyNameField, 2);
        add(usernameField, 2);
        add(emailField, 2);
        add(passwordLayout);
        add(submitButton, 2);
        add(cancelButton, 2);
    }

    @Override
    protected void register() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        String companyName = companyNameField.getValue();
        String passwordConfirmation = passwordConfirmationField.getValue();

        registerInterface.registerUnternehmen(username, password, email, companyName, passwordConfirmation);
        UI.getCurrent().navigate(Globals.Pages.LOGIN);
    }
}