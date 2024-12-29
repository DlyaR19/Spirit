package com.spirit.application.views.profile;


import com.spirit.application.dto.StudentDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.service.ProfileService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Style;

import java.io.InputStream;
import java.util.Base64;

/**
 * The ProfileBaseView class is an abstract base class that provides shared functionality
 * for displaying and managing user profiles. It handles profile details such as avatar,
 * description, and social links, and supports editing, uploading images, and deleting profiles.
 */
public abstract class ProfileBaseView extends Composite<VerticalLayout> {

    protected final transient ProfileService profileService;
    protected final transient SessionService sessionService;
    protected final transient VerticalLayout layout = new VerticalLayout();
    protected transient MarkdownConverter markdownConverter = new MarkdownConverter();
    protected Avatar avatar;
    protected H6 webseite;
    protected Div description;
    protected Upload upload;
    protected MemoryBuffer buffer;
    protected static final String OPACITY = "opacity";

    /**
     * Constructor for ProfileBaseView.
     * @param profileService the service for profile-related operations.
     * @param sessionService the service for session-related data.
     */
    protected ProfileBaseView(ProfileService profileService, SessionService sessionService) {
        this.profileService = profileService;
        this.sessionService = sessionService;
        layout.getStyle().setAlignItems(Style.AlignItems.FLEX_START);
        layout.getStyle().set("max-width", "1000px");
        layout.add(header(sessionService.getCurrentUser()), description(sessionService.getCurrentUser()));
        getContent().setSizeFull();
        getContent().getStyle().setAlignItems(Style.AlignItems.CENTER);
        getContent().add(layout);
    }

    /**
     * Creates a header layout containing the avatar, user details, and action buttons.
     * @param user the current user's data transfer object.
     * @return a HorizontalLayout containing the header components.
     */
    private HorizontalLayout header(UserDTO user) {
        HorizontalLayout header = new HorizontalLayout();
        VerticalLayout infoLayout = new VerticalLayout();
        avatar = new Avatar();
        avatar.setImage("data:image/jpeg;base64,"+user.getProfile().getAvatar());
        avatar.setHeight("150px");
        avatar.setWidth("150px");
        H2 username = new H2(setGreetingText());
        H6 email = new H6(user.getEmail());
        email.getStyle().set(OPACITY, "0.8");
        webseite = new H6(user.getProfile().getWebseite());
        webseite.getStyle().set(OPACITY, "0.8");
        HorizontalLayout emailLayout = new HorizontalLayout(
                new H6("Email: "), email);
        HorizontalLayout webseiteLayout = new HorizontalLayout(
                new H6("Webseite: "), webseite);

        Button deleteButton = new Button("Profil löschen", event -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Bestätigung");
            dialog.setText("Sind Sie sicher, dass Sie Ihr Profil löschen möchten?");

            // "Löschen"-Button
            dialog.setConfirmButton("Löschen", confirmEvent -> {
                profileService.deleteProfile(user.getProfile().getProfileID());
                Notification.show("Profil erfolgreich gelöscht.", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate(Globals.Pages.LOGIN); // Zur Startseite navigieren
            });

            // "Abbrechen"-Button
            dialog.setCancelButton("Abbrechen", cancelEvent -> dialog.close());

            dialog.open();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttonLayout = new HorizontalLayout(new Button("bearbeiten", event ->
                openEditDialog(user)), new Button("Bild hochladen", event -> openAvatarDialog(user)), deleteButton);

        if (isStudentUser()) {
            StudentDTO studentDTO = sessionService.getCurrentStudent();
            infoLayout.add(username, emailLayout, webseiteLayout, birthdateLayout(studentDTO), buttonLayout);
        } else {
            infoLayout.add(username, emailLayout, webseiteLayout, buttonLayout);
        }
        header.add(avatar, infoLayout);
        return header;
    }

    /**
     * Creates a description layout displaying the user's profile description.
     * @param user the current user's data transfer object.
     * @return a VerticalLayout containing the profile description.
     */
    private VerticalLayout description(UserDTO user) {
        description = new Div();
        description.getElement().setProperty(
                "innerHTML",
                markdownConverter.convertToHtml(user.getProfile().getProfileDescription())
        );


        return new VerticalLayout(
                new H3("Beschreibung: "),
                description
        );
    }

    /**
     * Opens a dialog for editing the user's profile description and website URL.
     * @param user the current user's data transfer object.
     */
    private void openEditDialog(UserDTO user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeight("500px");
        VerticalLayout editLayout = new VerticalLayout();
        HorizontalLayout inputLayout = new HorizontalLayout();
        TextField webseiteTexField = new TextField("Webseite: ");
        TextArea descriptionTextField = new TextArea("Beschreibung: ");
        descriptionTextField.setWidth("400px");
        descriptionTextField.setHeight("200px");
        inputLayout.add(webseiteTexField);
        HorizontalLayout descriptionLayout = new HorizontalLayout(descriptionTextField);
        Button save = new Button("Speichern");
        Button cancel = new Button("Abbrechen");
        if (webseiteTexField.getValue().isEmpty()) {
            webseiteTexField.setValue(user.getProfile().getWebseite() != null ? user.getProfile().getWebseite() : "");
        }
        if (descriptionTextField.getValue().isEmpty()) {
            descriptionTextField.setValue(user.getProfile().getProfileDescription() != null ? user.getProfile().getProfileDescription() : "");
        }
        save.addClickListener(event -> {
            try {
                profileService.saveSocials(
                        sessionService.getCurrentUser().getProfile(),
                        webseiteTexField.getValue(),
                        descriptionTextField.getValue()
                );
                updateProfileData(user);
                Notification.show("Daten erfolgreich gespeichert.");
            } catch (Exception e) {
                Notification.show("Fehler beim Speichern.");
            } finally {
                dialog.close(); // Dialog immer schließen, auch bei Fehlern
            }
        });
        cancel.addClickListener(event -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel);
        editLayout.add(inputLayout, descriptionLayout, buttonLayout);
        dialog.add(editLayout);
        dialog.open();
    }

    /**
     * Opens a dialog for uploading or deleting the user's profile avatar.
     * @param user the current user's data transfer object.
     */
    private void openAvatarDialog(UserDTO user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeight("500px");
        VerticalLayout avatarLayout = new VerticalLayout();
        buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button saveButton = new Button("Speichern", buttonClickEvent -> {
            try (InputStream inputStream = buffer.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                profileService.deleteProfileImage(user.getProfile().getProfileID());
                profileService.saveProfileImage(user.getProfile().getProfileID(), base64Image);
                user.getProfile().setAvatar(base64Image); // Hier das Avatar-Bild im UserDTO aktualisieren
                Notification.show("Bild erfolgreich hochgeladen");
            } catch (Exception e) {
                Notification.show("Fehler beim Hochladen des Bildes");
            }
            updateProfileData(user);
            dialog.close();
        });

        // Bild löschen
        Button deleteButton = new Button("Bild löschen", buttonClickEvent -> {
            profileService.deleteProfileImage(user.getProfile().getProfileID());
            user.getProfile().setAvatar(null);
            Notification.show("Bild erfolgreich gelöscht");
            updateProfileData(user);
            dialog.close();
        });

        Button cancelButton = new Button("Abbrechen", buttonClickEvent -> dialog.close());
        buttonLayout.add(saveButton, deleteButton ,cancelButton);
        avatarLayout.add(new H3("Profil Bild hochladen"), upload, buttonLayout);
        dialog.add(avatarLayout);
        dialog.open();
    }

    /**
     * Updates the profile data displayed in the view.
     * @param user the current user's data transfer object.
     */
    private void updateProfileData(UserDTO user) {
        webseite.setText(user.getProfile().getWebseite());
        description.getElement().setProperty(
                "innerHTML",
                markdownConverter.convertToHtml(user.getProfile().getProfileDescription())
        );
        avatar.setImage("data:image/jpeg;base64," + user.getProfile().getAvatar());
    }

    /**
     * Returns a greeting text based on the type of the current user.
     * @return a String containing the greeting message.
     */
    private String setGreetingText() {
        if (isBusinessUser()) {
            return "Hallo " + sessionService.getCurrentUnternehmen().getUsername() + "!";
        } else if (isStudentUser()) {
            return "Hallo " + sessionService.getCurrentStudent().getUsername() + "!";
        }
        return "";
    }

    /**
     * Checks if the current user is a business user.
     * @return true if the current user is a business user, false otherwise.
     */
    private boolean isBusinessUser() {
        try {
            return sessionService.getCurrentUnternehmen() != null;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Checks if the current user is a student user.
     * @return true if the current user is a student user, false otherwise.
     */
    private boolean isStudentUser() {
        try {
            return sessionService.getCurrentStudent() != null;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Creates a layout displaying the student's birthdate.
     * @param studentDTO the data transfer object for the student user.
     * @return a HorizontalLayout containing the birthdate information.
     */
    private HorizontalLayout birthdateLayout(StudentDTO studentDTO){
        HorizontalLayout birthdateLayout = new HorizontalLayout();
        H6 birthdate = new H6("Geburtsdatum: " + studentDTO.getBirthdate());
        birthdate.getStyle().set(OPACITY, "0.8");
        birthdateLayout.add(birthdate);
        return birthdateLayout;
    }
}
