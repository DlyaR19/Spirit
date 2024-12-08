package com.spirit.application.views.register;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.spirit.application.util.Globals;

@CssImport("./themes/spirit/views/RegisterView.css")
@PageTitle("Register")
@Route(value = Globals.Pages.SIGNUP)
@AnonymousAllowed
@Menu(order = 2)
public class RegisterView extends VerticalLayout {

    private final UnternehmenRegisterView unternehmenRegisterView;
    private final StudentRegisterView studentRegisterView;

    public RegisterView(RegisterProxy registerProxy) {
        String student = "Student";
        String unternehmen = "Unternehmen";

        // Layout-Zentrierung
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Überschrift
        H1 title = new H1("Registrieren");
        title.getStyle().set("text-align", "center");

        unternehmenRegisterView = new UnternehmenRegisterView(registerProxy);
        studentRegisterView = new StudentRegisterView(registerProxy);

        // Rolle auswählen
        RadioButtonGroup<String> roleSelector = new RadioButtonGroup<>();
        roleSelector.setLabel("Registrieren als:");
        roleSelector.setItems(student, unternehmen);

        // Dynamische Felder basierend auf der Auswahl
        Div dynamicFields = new Div();
        roleSelector.setValue(student); // Standardmäßig "Student" auswählen
        dynamicFields.add(studentRegisterView);

        roleSelector.addValueChangeListener(event -> {
            dynamicFields.removeAll();
            if (student.equals(event.getValue())) {
                dynamicFields.add(studentRegisterView);
            } else if (unternehmen.equals(event.getValue())) {
                dynamicFields.add(unternehmenRegisterView);
            }
        });

        // Formular-Layout
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.addClassName("form-layout");

        formLayout.add(title, roleSelector, dynamicFields);

        // Formular in einen Wrapper einfügen
        Div container = new Div();
        container.addClassName("register-container");
        container.add(formLayout);

        add(container);
    }
}