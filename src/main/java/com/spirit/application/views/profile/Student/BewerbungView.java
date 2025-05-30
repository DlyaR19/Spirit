package com.spirit.application.views.profile.Student;


import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

/**
 * This class represents a view where students can apply for a job position.
 * It provides an interface for students to view a job and interact with buttons
 * to perform actions related to the application process.
 */
@Route(value = Globals.Pages.BEWERBUNG_STUDENT, layout = AppView.class)
@RolesAllowed(Globals.Roles.STUDENT)
public class BewerbungView extends Composite<VerticalLayout> {
    private static final String MIN_CONTENT = "min-content";

    /**
     * Default constructor that sets up the layout and components of the Bewerbung view.
     * The layout contains a header, two buttons, and positions them properly within the layout.
     */
    public BewerbungView() {
        H2 h2 = new H2();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonPrimary2 = new Button();
        getContent().setWidth(MIN_CONTENT);
        getContent().setHeight(MIN_CONTENT);
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);


        h2.setText("Möchtest du dich auf die Stelle ");


        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, h2);
        h2.setWidth("max-content");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth(MIN_CONTENT);
        layoutRow.setHeight(MIN_CONTENT);
        buttonPrimary.setText("Button");
        buttonPrimary.setWidth(MIN_CONTENT);
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary2.setText("Button");
        buttonPrimary2.setWidth(MIN_CONTENT);
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_ERROR);
        getContent().add(h2);
        getContent().add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonPrimary2);
    }

}
