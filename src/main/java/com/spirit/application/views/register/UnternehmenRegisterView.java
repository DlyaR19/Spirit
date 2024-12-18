package com.spirit.application.views.register;

import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.service.impl.RegisterInterfaceImpl;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
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

    private void setupUnternehmenForm() {
        companyNameField = new TextField("Name des Unternehmens");
        TextField branchNameField = new TextField("Branche");


        add(companyNameField, 2);
        add(usernameField, 2);
        add(emailField, 2);
        add(branchNameField, 2);
        add(passwordField, 1);
        add(passwordConfirmationField, 1);
        add(cancelButton, 1);
        add(submitButton, 1);
    }

    @Override
    protected void register() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        String companyName = companyNameField.getValue();
        String passwordConfirmation = passwordConfirmationField.getValue();

        try{
            registerInterface.registerUnternehmen(username, password, email, companyName, passwordConfirmation);
            UI.getCurrent().navigate(Globals.Pages.LOGIN);
            Notification
                    .show("Registrierung erfolgreich!", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (RegisterInterfaceImpl.UsernameAlreadyTakenException |
                 RegisterInterfaceImpl.EmailAlreadyTakenException |
                 IllegalArgumentException ex) {
            Notification
                    .show("Fehler: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}