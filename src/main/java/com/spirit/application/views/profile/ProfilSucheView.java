package com.spirit.application.views.profile;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.service.ProfilService;
import com.spirit.application.service.UserService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

/**
 * View für die Profilsuche
 */
@RolesAllowed({Globals.Roles.STUDENT, Globals.Roles.UNTERNEHMEN})
@Route(value = Globals.Pages.PROFIL_SUCHE, layout = AppView.class)
public class ProfilSucheView extends VerticalLayout {
    private final ProfilService profilService;
    private final UserService userService;
    private final Grid<UserDTO> profilGrid;
    private final TextField suchFeld;
    private final ComboBox<String> filterTyp;

    public ProfilSucheView(ProfilService profilService, UserService userService) {
        this.profilService = profilService;
        this.userService = userService;

        // Layout Konfiguration
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Überschrift
        H2 title = new H2("Profile durchsuchen");
        add(title);

        // Suchbereich
        HorizontalLayout suchBereich = new HorizontalLayout();
        suchFeld = new TextField();
        suchFeld.setPlaceholder("Nach Benutzername suchen...");
        suchFeld.setClearButtonVisible(true);
        suchFeld.setValueChangeMode(ValueChangeMode.LAZY);
        suchFeld.addValueChangeListener(e -> updateListe());

        // Filter ComboBox für den Benutzertyp (Alle, Studenten, Unternehmen)
        filterTyp = new ComboBox<>();
        filterTyp.setItems("Alle", "Studenten", "Unternehmen");
        filterTyp.setValue("Alle");
        filterTyp.addValueChangeListener(e -> updateListe());

        suchBereich.add(suchFeld, filterTyp);
        add(suchBereich);

        // Grid für Profile
        profilGrid = new Grid<>();
        profilGrid.addColumn(user -> user.getProfil().getProfilID()).setHeader("Profil ID").setVisible(false);
        profilGrid.addColumn(UserDTO::getUsername).setHeader("Benutzername");
        profilGrid.addColumn(this::getProfilTyp).setHeader("Typ");
        profilGrid.addColumn(user -> {
            Double rating = user.getProfil().getAvgRating();
            return rating != null ? String.format("%.1f ★", rating) : "Keine";
        }).setHeader("Bewertung");

        // Profilbild als Avatar
        profilGrid.addComponentColumn(user -> {
            Avatar avatar = new Avatar();
            if (user.getProfil().getAvatar() != null) {
                avatar.setImage("data:image/jpeg;base64," + user.getProfil().getAvatar());
            }
            avatar.setHeight("40px");
            avatar.setWidth("40px");
            return avatar;
        }).setHeader("Bild");

        // Aktionen Spalte
        profilGrid.addComponentColumn(user -> {
            Button anzeigenButton = new Button("Anzeigen", VaadinIcon.EYE.create());
            anzeigenButton.addClickListener(e -> profilAnzeigen(user));
            return anzeigenButton;
        }).setHeader("Aktionen");

        profilGrid.setHeight("100%");
        add(profilGrid);

        // Initial Liste laden
        updateListe();
    }

    private void updateListe() {
        String suchBegriff = suchFeld.getValue().toLowerCase();
        String selectedUserType = filterTyp.getValue();

        List<UserDTO> gefiltert = userService.searchUsers(suchBegriff, selectedUserType);

        profilGrid.setItems(gefiltert);
    }

    private String getProfilTyp(UserDTO user) {
        // Hier die Logik zum Bestimmen des Typs implementieren
        if (userService.isStudent(user.getUserID())) {
            return "Student";
        } else if (userService.isUnternehmen(user.getUserID())) {
            return "Unternehmen";
        }
        return "Unbekannt";
    }

    private void profilAnzeigen(UserDTO user) {
        Dialog profilDialog = new Dialog();
        profilDialog.setWidth("800px");
        profilDialog.setHeight("600px");

        VerticalLayout profilLayout = new VerticalLayout();
        profilLayout.setSpacing(true);
        profilLayout.setPadding(true);

        // Header mit Profilbild und Basis-Info
        HorizontalLayout headerLayout = new HorizontalLayout();
        Avatar avatar = new Avatar();
        if (user.getProfil().getAvatar() != null) {
            avatar.setImage("data:image/jpeg;base64," + user.getProfil().getAvatar());
        }
        avatar.setHeight("150px");
        avatar.setWidth("150px");

        VerticalLayout infoLayout = new VerticalLayout();
        H2 username = new H2(user.getUsername());
        H4 typ = new H4(getProfilTyp(user));

        // Bewertungsanzeige
        HorizontalLayout ratingLayout = new HorizontalLayout();
        if (user.getProfil().getAvgRating() != null) {
            H4 rating = new H4(String.format("%.1f ★", user.getProfil().getAvgRating()));
            Span totalRatings = new Span(String.format("(%d Bewertungen)",
                    user.getProfil().getTotalRating() != null ? user.getProfil().getTotalRating() : 0));
            totalRatings.getStyle().set("opacity", "0.6");
            ratingLayout.add(rating, totalRatings);
        } else {
            H4 noRating = new H4("Noch keine Bewertungen");
            ratingLayout.add(noRating);
        }

        infoLayout.add(username, typ, ratingLayout);
        headerLayout.add(avatar, infoLayout);

        // Profil Details
        VerticalLayout detailsLayout = new VerticalLayout();
        if (user.getProfil().getWebseite() != null && !user.getProfil().getWebseite().isEmpty()) {
            H4 websiteLabel = new H4("Website:");
            Anchor website = new Anchor(user.getProfil().getWebseite(), user.getProfil().getWebseite());
            detailsLayout.add(websiteLabel, website);
        }

        if (user.getProfil().getProfileDescription() != null && !user.getProfil().getProfileDescription().isEmpty()) {
            H4 descLabel = new H4("Beschreibung:");
            Div description = new Div();
            description.getElement().setProperty("innerHTML",
                    new MarkdownConverter().convertToHtml(user.getProfil().getProfileDescription()));
            detailsLayout.add(descLabel, description);
        }

        UserDTO currentUser = userService.getCurrentUser();
        if (!(user.getUserID() == currentUser.getUserID())) {
            Button rateButton = new Button("Bewerten", e -> {
                if (userService.hasRated(currentUser, user)) {
                    Notification.show("Sie haben diesen Benutzer bereits bewertet.", 3000, Notification.Position.MIDDLE);
                } else {
                    profilDialog.close();
                    openRatingDialog(user);
                }
            });
            rateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button chatButton = new Button("Chat starten", e -> {
                getUI().ifPresent(ui -> ui.navigate(Globals.Pages.CHAT + "/" + user.getUserID()));
                profilDialog.close();
            });
            chatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            HorizontalLayout buttonLayout = new HorizontalLayout(rateButton, chatButton);
            buttonLayout.setSpacing(true);
            detailsLayout.add(buttonLayout);
        }

        // Schließen Button
        Button closeButton = new Button("Schließen", e -> profilDialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        profilLayout.add(headerLayout, detailsLayout, closeButton);
        profilDialog.add(profilLayout);
        profilDialog.open();
    }

    private void openRatingDialog(UserDTO user) {
        Dialog ratingDialog = new Dialog();
        ratingDialog.setWidth("400px");
        ratingDialog.setHeight("300px");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);

        // Header
        H2 title = new H2("Bewertung für " + user.getUsername());
        layout.add(title);

        // Rating selection
        ComboBox<Integer> ratingComboBox = new ComboBox<>("Bewertung");
        ratingComboBox.setItems(1, 2, 3, 4, 5);
        ratingComboBox.setPlaceholder("Wählen Sie eine Bewertung");
        ratingComboBox.setValue(5); // default to 5 stars

        // Submit Button
        Button submitButton = new Button("Bewertung abgeben", event -> {
            Integer rating = ratingComboBox.getValue();
            if (rating != null) {
                userService.rateUser(user.getUserID(), rating);
                ratingDialog.close();
                Notification.show("Bewertung erfolgreich abgegeben!");
            } else {
                Notification.show("Bitte wählen Sie eine Bewertung!");
            }
        });

        layout.add(ratingComboBox, submitButton);

        // Close Button
        Button closeButton = new Button("Schließen", event -> ratingDialog.close());
        layout.add(closeButton);

        ratingDialog.add(layout);
        ratingDialog.open();
    }
}