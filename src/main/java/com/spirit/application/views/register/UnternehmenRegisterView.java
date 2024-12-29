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

/**
 * View for registering a new "Unternehmen" (company) account in the application.
 * <p>This view extends {@link BaseRegisterView} and customizes the registration form
 * for companies by including fields specific to companies, such as the company name
 * and branch.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @Component}: Marks this class as a Spring-managed component.</li>
 * </ul>
 */
@Component
public class UnternehmenRegisterView extends BaseRegisterView {

    private TextField companyNameField;

    /**
     * Constructs a new {@code UnternehmenRegisterView}.
     * @param registerInterface the interface handling registration logic.
     */
    @Autowired
    public UnternehmenRegisterView(@Qualifier("registerProxy") RegisterInterface registerInterface) {
        super(registerInterface);
        setupUnternehmenForm();
    }

    /**
     * Configures the registration form for a company account by adding relevant fields,
     * such as company name, email, username, and password.
     */
    private void setupUnternehmenForm() {
        companyNameField = new TextField("Name des Unternehmens");
        TextField branchNameField = new TextField("Branche");

        companyNameField.setRequiredIndicatorVisible(true);
        usernameField.setRequiredIndicatorVisible(true);
        emailField.setRequiredIndicatorVisible(true);
        emailField.setPattern(
                "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
        passwordField.setRequiredIndicatorVisible(true);
        passwordConfirmationField.setRequiredIndicatorVisible(true);

        add(companyNameField, 2);
        add(usernameField, 2);
        add(emailField, 2);
        add(branchNameField, 2);
        add(passwordField, 2);
        add(passwordConfirmationField, 2);
        add(cancelButton, 1);
        add(submitButton, 1);
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