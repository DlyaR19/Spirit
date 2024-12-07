package com.spirit.application.views.register;

import com.spirit.application.views.main.MainView;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Register")
@Route(value = Globals.Pages.SIGNUP, layout = MainView.class) // MainView als übergeordnetes Layout
@AnonymousAllowed
public class RegisterView extends FormLayout {

    private final UnternehmenRegisterView unternehmenRegisterView;
    private final StudentRegisterView studentRegisterView;

    public RegisterView(RegisterProxy registerProxy) {
        // Layout und Überschrift einrichten
        setupLayout();

        // Überschrift
        H1 title = new H1("Registrieren");
        title.getStyle()
                .set("text-align", "center")
                .set("font-size", "24px")
                .set("margin-bottom", "20px");

        // Einleitungstext
        Paragraph intro = new Paragraph("Bitte wählen Sie aus, ob Sie sich als Student oder Unternehmen registrieren möchten.");
        intro.getStyle()
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        // Views für dynamische Felder
        unternehmenRegisterView = new UnternehmenRegisterView(registerProxy);
        studentRegisterView = new StudentRegisterView(registerProxy);

        // Auswahl zwischen Student und Unternehmen
        RadioButtonGroup<String> roleSelector = new RadioButtonGroup<>();
        roleSelector.setLabel("Registrieren als:");
        roleSelector.setItems("Student", "Unternehmen");

        // Dynamischer Formularbereich
        FormLayout dynamicFields = new FormLayout();

        // Standardwert und Initialeinstellungen
        roleSelector.setValue("Student");
        dynamicFields.add(studentRegisterView);

        // Umschalten zwischen den Views
        roleSelector.addValueChangeListener(event -> {
            dynamicFields.removeAll();
            if ("Student".equals(event.getValue())) {
                dynamicFields.add(studentRegisterView);
            } else if ("Unternehmen".equals(event.getValue())) {
                dynamicFields.add(unternehmenRegisterView);
            }
        });

        // Elemente zum Layout hinzufügen
        add(title, intro, roleSelector, dynamicFields);
    }

    private void setupLayout() {
        addClassName("register-view");
        setMaxWidth("500px");
        getStyle()
                .set("margin", "0 auto")
                .set("padding", "20px")
                .set("border", "1px solid #ddd")
                .set("border-radius", "8px")
                .set("background-color", "#ffffff")
                .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)");
    }
}