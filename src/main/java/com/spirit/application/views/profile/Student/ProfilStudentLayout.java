package com.spirit.application.views.profile.Student;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

/*
    Dialog View Layout
 */


@Getter
//@CssImport("./styles/index.css")
public class ProfilStudentLayout extends VerticalLayout {

    private final TextField tfVorname = new TextField("Vorname:");
    private final TextField tfNachname = new TextField("Nachname:");
    private final TextField interestField = new TextField("Interessen:");
    private final TextField skillsField = new TextField("Kompetenzen:");
    private final TextField degreeField = new TextField("Studiengang: ");
    private final TextField linkedinUsernameField = new TextField("Linkedin Username:");
    private final TextField profileDescriptionField = new TextField("Profilbeschreibung:");

    private final TextField avatarUrlField = new TextField("Avatar URL:");

    public ProfilStudentLayout() {
        addClassName("profile-form");

        HorizontalLayout layoutName = new HorizontalLayout();
        HorizontalLayout layoutAdresse = new HorizontalLayout();
        HorizontalLayout layoutInterestSkillDegree = new HorizontalLayout();
        HorizontalLayout layoutLinkedin = new HorizontalLayout();
        HorizontalLayout layoutProfileDescription = new HorizontalLayout();
        HorizontalLayout layoutAvatar = new HorizontalLayout();

        layoutName.add(tfVorname, tfNachname);

        layoutInterestSkillDegree.add(interestField, skillsField, degreeField);
        layoutLinkedin.add(linkedinUsernameField);
        layoutProfileDescription.add(profileDescriptionField);
        layoutAvatar.add(avatarUrlField);

        add(
                layoutName,
                layoutAdresse,
                layoutInterestSkillDegree,
                layoutLinkedin,
                layoutProfileDescription,
                layoutAvatar
        );
    }
}
