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

import java.time.LocalDate;

/**
 * View for registering a new "Student" account in the application.
 * <p>This view extends {@link BaseRegisterView} and customizes the registration form
 * for students by including fields specific to students, such as first name, last name,
 * and date of birth.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @Component}: Marks this class as a Spring-managed component.</li>
 * </ul>
 */
@Component
public class StudentRegisterView extends BaseRegisterView {

    private TextField textFieldFirstName;
    private TextField textFieldLastName;
    private DatePicker birth;

    /**
     * Constructs a new {@code StudentRegisterView}.
     *
     * @param registerInterface the interface handling registration logic.
     */
    @Autowired
    public StudentRegisterView(@Qualifier("registerProxy") RegisterInterface registerInterface) {
        super(registerInterface);
        setupStudentForm();
    }

    /**
     * Configures the registration form for a student account by adding relevant fields,
     * such as first name, last name, date of birth, and program of study.
     */
    private void setupStudentForm() {
        textFieldFirstName = new TextField("Vorname");
        textFieldLastName = new TextField("Nachname");
        DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
        TextField studiengang = new TextField("Studiengang");

        textFieldFirstName.setRequiredIndicatorVisible(true);
        textFieldLastName.setRequiredIndicatorVisible(true);
        geburtsdatum.setRequired(true);
        geburtsdatum.setRequiredIndicatorVisible(true);
        usernameField.setRequiredIndicatorVisible(true);
        emailField.setRequiredIndicatorVisible(true);
        emailField.setPattern(
                "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
        passwordField.setRequiredIndicatorVisible(true);
        passwordConfirmationField.setRequiredIndicatorVisible(true);

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

        this.birth = geburtsdatum;
    }

    /**
     * Handles the registration process by collecting user input and calling the
     * appropriate method in {@link RegisterInterface}.
     */
    @Override
    protected void register() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        String firstName = this.textFieldFirstName.getValue();
        String lastName = this.textFieldLastName.getValue();
        String passwordConfirmation = passwordConfirmationField.getValue();
        LocalDate birth = this.birth.getValue();

        try {
            registerInterface.registerStudent(username, password, email, firstName, lastName, passwordConfirmation, birth);
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