package com.spirit.application.views.profile.Student;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

/**
 * A layout component that displays and manages the student's profile form.
 * Contains fields for first name, last name, interests, skills, degree, website, profile description, and avatar URL.
 * <p>This class defines the form layout and organizes the components into horizontal sections.</p>
 */
@Getter
public class ProfilStudentLayout extends VerticalLayout {

    private final TextField tfVorname = new TextField("Vorname:");
    private final TextField tfNachname = new TextField("Nachname:");
    private final TextField interestField = new TextField("Interessen:");
    private final TextField skillsField = new TextField("Kompetenzen:");
    private final TextField degreeField = new TextField("Studiengang: ");
    private final TextField webseiteField = new TextField("Webseite:");
    private final TextField profileDescriptionField = new TextField("Profilbeschreibung:");
    private final TextField avatarUrlField = new TextField("Avatar URL:");

    /**
     * Constructs a ProfilStudentLayout with input fields for student profile information.
     * The fields are organized into horizontal layouts and added to the main vertical layout.
     */
    public ProfilStudentLayout() {
        addClassName("profile-form");

        HorizontalLayout layoutName = new HorizontalLayout();
        HorizontalLayout layoutAdresse = new HorizontalLayout();
        HorizontalLayout layoutInterestSkillDegree = new HorizontalLayout();
        HorizontalLayout layoutWebseite = new HorizontalLayout();
        HorizontalLayout layoutProfileDescription = new HorizontalLayout();
        HorizontalLayout layoutAvatar = new HorizontalLayout();

        layoutName.add(tfVorname, tfNachname);

        layoutInterestSkillDegree.add(interestField, skillsField, degreeField);
        layoutWebseite.add(webseiteField);
        layoutProfileDescription.add(profileDescriptionField);
        layoutAvatar.add(avatarUrlField);

        add(
                layoutName,
                layoutAdresse,
                layoutInterestSkillDegree,
                layoutWebseite,
                layoutProfileDescription,
                layoutAvatar
        );
    }
}
