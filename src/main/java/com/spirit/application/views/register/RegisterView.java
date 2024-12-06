package com.spirit.application.views.register;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.spirit.application.util.Globals;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Register")
@Route(value = Globals.Pages.SIGNUP)
@AnonymousAllowed
@Menu(order = 2)
public class RegisterView extends FormLayout {

    private final UnternehmenRegisterView unternehmenRegisterView;
    private final StudentRegisterView studentRegisterView;


    public RegisterView(RegisterProxy registerProxy) {
        String student = "Student";
        String unternehmen = "Unternehmen";

        setupLayout();

        unternehmenRegisterView = new UnternehmenRegisterView(registerProxy);
        studentRegisterView = new StudentRegisterView(registerProxy);

        RadioButtonGroup<String> roleSelector = new RadioButtonGroup<>();
        roleSelector.setLabel("Registrieren als:");
        roleSelector.setItems(student, unternehmen);

        FormLayout dynamicFields = new FormLayout();

        roleSelector.setValue(student);
        dynamicFields.add(studentRegisterView);

        roleSelector.addValueChangeListener(event -> {
            dynamicFields.removeAll();
            if (student.equals(event.getValue())) {
                dynamicFields.add(studentRegisterView);
            } else if (unternehmen.equals(event.getValue())) {
                dynamicFields.add(unternehmenRegisterView);
            }
        });

        add(roleSelector, dynamicFields);
    }

    private void setupLayout() {
        addClassName("sign-in");
        setMaxWidth("500px");
    }
}