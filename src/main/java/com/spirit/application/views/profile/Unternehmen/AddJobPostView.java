package com.spirit.application.views.profile.Unternehmen;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.service.JobPostService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.EntityFactory;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;
import java.time.LocalDate;

/**
 * View for adding a new job post. This page is accessible only to users with the "UNTERNEHMEN" role.
 * <p>The view provides a form where users can enter details about a job posting, including
 * title, employment type, location, and description. The form also includes validation for required fields
 * and options to save or reset the input fields.</p>
 * <p>On successful submission, the job post is saved to the database and the user is redirected
 * to their job postings page.</p>
 */
@PageTitle("Add JobPost")
@Route(value = Globals.Pages.JOBPOST, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)
public class AddJobPostView extends Composite<VerticalLayout> {

    private final transient EntityFactory entityFactory;
    private final transient SessionService sessionService;
    private final transient JobPostService jobPostService;
    private static final String DELETE = "Löschen";

    /**
     * Constructs the AddJobPostView.
     * @param entityFactory     The factory for creating job post entities.
     * @param sessionService    The session management service.
     * @param jobPostService    The service for managing job posts.
     * @param markdownConverter The markdown converter (not currently used in this implementation).
     */
    @Autowired
    public AddJobPostView(EntityFactory entityFactory, SessionService sessionService, JobPostService jobPostService, MarkdownConverter markdownConverter) {
        this.entityFactory = entityFactory;
        this.sessionService = sessionService;
        this.jobPostService = jobPostService;
        setUpUI();
    }

    /**
     * Sets up the user interface for adding a job post.
     * <p>
     * The UI includes:
     * <ul>
     *     <li>A text field for the job title.</li>
     *     <li>A combo box for selecting the employment type.</li>
     *     <li>A text field for the job location.</li>
     *     <li>A text area for the job description.</li>
     *     <li>Save and reset buttons.</li>
     * </ul>
     * Includes validation to ensure that all required fields are filled out before saving.
     * </p>
     */
    private void setUpUI() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        TextField title = new TextField("Titel: ");
        title.setWidth("100%"); // Vergrößert die Breite des Titel-Feldes
        titleLayout.add(title);
        titleLayout.setWidth("100%");

        HorizontalLayout infoLayout = new HorizontalLayout();
        ComboBox<String> comboBox = new ComboBox<>("Anstellungsart: ");
        TextField location = new TextField("Standort: ");
        comboBox.setWidth("50%"); // Stellt sicher, dass der Kombinationsfeld die Hälfte des verfügbaren Platzes einnimmt
        location.setWidth("50%"); // Stellt sicher, dass das Standortfeld die Hälfte des verfügbaren Platzes einnimmt
        comboBox.setItems("Minijob", "Teilzeit", "Vollzeit", "Praktikum", "Bachelorprojekt", "Masterprojekt", "Büro", "Homeoffice");
        infoLayout.add(comboBox, location);
        infoLayout.setWidth("100%"); // Stellt sicher, dass das Layout die volle Breite einnimmt

        H3 h3 = new H3("Stellenanzeige hinzufügen");
        H6 subtitle = new H6("Stellenausschreibungen mit Markdown schreiben");
        TextArea textArea = new TextArea("Beschreibung");
        textArea.setWidth("100%");
        textArea.setHeight("200px");

        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidthFull();
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        layoutColumn2.setPadding(true);

        FormLayout formLayout2Col = new FormLayout();
        formLayout2Col.setWidth("100%");
        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.setWidthFull();
        layoutRow.setHeight("min-content");
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);

        Button save = new Button("Speichern");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button(DELETE);

        save.addClickListener(event -> {
            // Reset previous validation states
            title.setInvalid(false);
            comboBox.setInvalid(false);
            location.setInvalid(false);
            textArea.setInvalid(false);

            // Validate inputs
            boolean isValid = true;

            // Check title
            if (title.getValue() == null || title.getValue().trim().isEmpty()) {
                title.setInvalid(true);
                title.setErrorMessage("Bitte Titel eingeben");
                isValid = false;
            }

            // Check employment type
            if (comboBox.getValue() == null || comboBox.getValue().trim().isEmpty()) {
                comboBox.setInvalid(true);
                comboBox.setErrorMessage("Bitte Anstellungsart auswählen");
                isValid = false;
            }

            // Check location
            if (location.getValue() == null || location.getValue().trim().isEmpty()) {
                location.setInvalid(true);
                location.setErrorMessage("Bitte Standort eingeben");
                isValid = false;
            }

            // Check description
            if (textArea.getValue() == null || textArea.getValue().trim().isEmpty()) {
                textArea.setInvalid(true);
                textArea.setErrorMessage("Bitte Beschreibung eingeben");
                isValid = false;
            }

            // If any validation fails, show error and stop
            if (!isValid) {
                Notification.show("Bitte alle Pflichtfelder ausfüllen", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // If all validations pass, create and save job post
            JobPost jobPost = entityFactory.createJobPost(
                    comboBox.getValue(),
                    title.getValue(),
                    location.getValue(),
                    textArea.getValue(),
                    sessionService.getCurrentUnternehmen().getUnternehmen(),
                    Date.valueOf(LocalDate.now())
            );
            jobPostService.saveJobPost(jobPost);

            // Show success notification
            Notification.show("Stellenanzeige erfolgreich hinzugefügt", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Clear input fields
            comboBox.clear();
            title.clear();
            location.clear();
            textArea.clear();
            UI.getCurrent().navigate(Globals.Pages.MY_JOBPOSTS);
        });


        cancel.addClickListener(event -> {
            comboBox.clear();
            title.clear();
            location.clear();
            textArea.clear();
        });

        layoutRow.add(save, cancel);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        layoutColumn2.add(h3, subtitle, titleLayout, infoLayout, formLayout2Col, textArea, layoutRow);
        getContent().add(layoutColumn2);
    }
}
