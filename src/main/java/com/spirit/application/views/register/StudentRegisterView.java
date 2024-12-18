package com.spirit.application.views.register;


import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.service.impl.RegisterInterfaceImpl;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StudentRegisterView extends BaseRegisterView {

    private TextField textFieldFirstName;
    private TextField textFieldLastName;

    @Autowired
    public StudentRegisterView(@Qualifier("registerProxy") RegisterInterface registerInterface) {
        super(registerInterface);
        setupStudentForm();
    }

    private void setupStudentForm() {
        textFieldFirstName = new TextField("Vorname");
        textFieldLastName = new TextField("Nachname");
        DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
        TextField studiengang = new TextField("Studiengang");

        add(textFieldFirstName, 1);
        add(textFieldLastName, 1);
        add(geburtsdatum, 1);
        add(studiengang, 1);
        add(usernameField, 2);
        add(emailField, 2);
        add(passwordField, 2);
        add(passwordConfirmationField, 2);
        add(cancelButton, 1);
        add(submitButton, 1);
    }

    @Override
    protected void register() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        String firstName = this.textFieldFirstName.getValue();
        String lastName = this.textFieldLastName.getValue();
        String passwordConfirmation = passwordConfirmationField.getValue();

        try {
            registerInterface.registerStudent(username, password, email, firstName, lastName, passwordConfirmation);
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