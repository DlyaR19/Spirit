package com.spirit.application.views.register;

import com.spirit.application.util.Globals;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Main registration view that allows users to register as either a student or a company.
 * <p>Includes dynamic form fields based on the selected role.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @CssImport}: Imports custom CSS for styling the view.</li>
 *   <li>{@code @PageTitle}: Sets the page title to "Registrierung".</li>
 *   <li>{@code @Route}: Defines the route for the registration page.</li>
 *   <li>{@code @AnonymousAllowed}: Allows access to the page without authentication.</li>
 *   <li>{@code @Menu}: Adds the page to the application menu.</li>
 * </ul>
 */
@CssImport("./themes/spirit/views/RegisterView.css")
@PageTitle("Registrierung")
@Route(value = Globals.Pages.SIGNUP)
@AnonymousAllowed
@Menu(order = 2)
public class RegisterView extends VerticalLayout {

    private final UnternehmenRegisterView unternehmenRegisterView;
    private final StudentRegisterView studentRegisterView;

    /**
     * Constructs the RegisterView with the required dependencies and initializes the layout.
     * @param registerProxy the proxy service used for handling registration logic.
     */
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
        roleSelector.setWidthFull();
        roleSelector.setItems(student, unternehmen);

        // Dynamische Felder basierend auf der Auswahl
        Div dynamicFields = new Div();
        roleSelector.setValue(student); // Standardmäßig "Student" auswählen
        dynamicFields.add(studentRegisterView);

        roleSelector.addValueChangeListener(event -> {
            dynamicFields.removeAll();
            if (event.getValue().equals(student)) {
                dynamicFields.add(studentRegisterView);
            } else {
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