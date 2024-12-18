package com.spirit.application.views.profile.Student;


import com.spirit.application.dto.JobPostDTO;
import com.spirit.application.service.BewerbungService;
import com.spirit.application.service.JobPostService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.EntityFactory;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Route(value = Globals.Pages.SUCHE_STUDENT, layout = AppView.class)
@RolesAllowed(Globals.Roles.STUDENT)
public class SuchView extends Composite<VerticalLayout> {

    private static final String FONT_WEIGHT = "font-weight";
    private static final String INNER_HTML = "innerHTML";

    private final VerticalLayout layout;
    private final List<JobPostDTO> jobPosts = new ArrayList<>();
    private final transient EntityFactory entityFactory = new EntityFactory();
    private final transient MarkdownConverter markdownConverter = new MarkdownConverter();
    private final transient SessionService sessionService;
    private final transient BewerbungService bewerbungService;
    private final transient JobPostService jobPostService;
    private final String[] comboBoxItems = {
            "Minijob", "Teilzeit", "Vollzeit", "Praktikum", "Bachelorprojekt",
            "Masterprojekt", "Büro", "Homeoffice",
    };

    @Autowired
    public SuchView(JobPostService jobPostService, SessionService sessionService,
                    BewerbungService bewerbungService) {
        this.sessionService = sessionService;
        this.bewerbungService = bewerbungService;
        this.jobPostService = jobPostService;
        this.layout = new VerticalLayout();
        this.layout.getStyle().setAlignItems(Style.AlignItems.CENTER);

        jobPostService.getAllJobPost().forEach(jobpost -> jobPosts.add(new JobPostDTO(jobpost)));


        updateJobPostList(jobPosts);
        getContent().getStyle().setAlignItems(Style.AlignItems.CENTER);
        getContent().add(searchbar(), layout);
    }

    public HorizontalLayout searchbar() {
        HorizontalLayout search = new HorizontalLayout();
        ComboBox<String> employmentType = new ComboBox<>();
        employmentType.setItems(comboBoxItems);
        Button searchButton = new Button("Suche");
        Button clearSearch = new Button("Neue Suche");
        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("Standort, Beschreibung, usw.");
        searchTextField.setWidth("100%");
        search.setWidth("100%");
        search.setMaxWidth("700px");
        search.add(employmentType, searchTextField, searchButton, clearSearch);
        searchButton.addClickListener(event -> {
            String searchText = searchTextField.getValue();
            String selectedAnstellungsart = employmentType.getValue();
            performSearch(searchText, selectedAnstellungsart);
        });
        clearSearch.addClickListener(event -> {
            searchTextField.clear();
            employmentType.clear();
        });
        return search;
    }

    private void performSearch(String searchText, String anstellungsart) {
        List<JobPostDTO> searchedJobPosts = jobPosts.stream()
                .filter(jobPost -> (anstellungsart == null || anstellungsart.isEmpty() || jobPost.getAnstellungsart().equalsIgnoreCase(anstellungsart)) &&
                        (searchText == null || searchText.isEmpty() || jobPostMatchesSearchText(jobPost, searchText)))
                .toList();
        updateJobPostList(searchedJobPosts);
    }

    private boolean jobPostMatchesSearchText(JobPostDTO jobPost, String searchText) {
        return jobPost.getTitel().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getBeschreibung().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getUnternehmen().getName().toLowerCase().contains(searchText.toLowerCase())
                || jobPost.getStandort().toLowerCase().contains(searchText.toLowerCase());
    }
    private void updateJobPostList(List<JobPostDTO> jobPostToDisplay) {
        layout.removeAll();
        jobPostToDisplay.forEach(jobPostDTO -> {
            layout.add(createCard(jobPostDTO));
        });

    }

    public VerticalLayout createCard(JobPostDTO jobPost) {
        VerticalLayout cardLayout = new VerticalLayout();
        Avatar avatar = new Avatar();
        avatar.setImage("data:image/jpeg;base64," + jobPost.getUnternehmen().getUser().getProfile().getAvatar());
        HorizontalLayout avatarLayout = new HorizontalLayout();
        avatarLayout.add(avatar, new H5(jobPost.getUnternehmen().getName()));
        H3 title = new H3(jobPost.getTitel());
        Button type = new Button(jobPost.getAnstellungsart());
        type.setWidth("min-content");
        type.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        type.setEnabled(true);
        HorizontalLayout dateLayout = new HorizontalLayout(
                new H4("Datum: "),
                new Span(jobPost.getVeroeffentlichungsdatum().toString())
        );
        HorizontalLayout locationLayout = new HorizontalLayout(
                new H4("Standort: "),
                new Span(jobPost.getStandort())
        );
        HorizontalLayout infoLayout = new HorizontalLayout(dateLayout, locationLayout);
        H4 profileDescription = new H4("Über uns ");
        Div profileDescriptionParagraph = new Div();
        profileDescriptionParagraph.getElement().setProperty(INNER_HTML, markdownConverter.convertToHtml(
                jobPost.getUnternehmen().getUser().getProfile().getProfileDescription())
        );
        VerticalLayout contactLayout = new VerticalLayout();
        contactLayout.add(createContactLayout("Email: ", jobPost.getUnternehmen().getUser().getEmail()));
        contactLayout.add(createContactLayout("Webseite: ", jobPost.getUnternehmen().getUser().getProfile().getWebseite()));
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button learnMore = new Button("Mehr erfahren");
        buttonLayout.add(learnMore);
        Long viewCount = jobPostService.getViewCount(jobPost.getJobPost());
        Span viewCountSpan = new Span("Aufrufe: " + viewCount);
        viewCountSpan.getStyle().set("font-size", "0.8em");
        viewCountSpan.getStyle().set("color", "gray");
        cardLayout.add(title, avatarLayout, type, infoLayout, contactLayout, profileDescription,
                profileDescriptionParagraph, buttonLayout, viewCountSpan);
        cardLayout.setWidth("100%");
        cardLayout.setMaxWidth("700px");
        cardLayout.getStyle().set("border", "1px solid #ccc");
        cardLayout.getStyle().set("border-radius", "8px");
        cardLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");

        learnMore.addClickListener(event -> openDialog(jobPost));

        return cardLayout;
    }

    private HorizontalLayout createContactLayout(String labelText, String valueText) {
        Span label = new Span(labelText);
        label.getStyle().set(FONT_WEIGHT, "bold");
        return new HorizontalLayout(label, new Span(valueText));
    }

    private void openDialog(JobPostDTO vacancy) {
        jobPostService.incrementViewCount(vacancy.getJobPost());
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        Avatar avatar = new Avatar();
        avatar.setImage("data:image/jpeg;base64," + vacancy.getUnternehmen().getUser().getProfile().getAvatar());
        H2 title = new H2(vacancy.getTitel());
        Button type = new Button(vacancy.getAnstellungsart());
        type.setWidth("min-content");
        type.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        type.setEnabled(true);

        HorizontalLayout dateLayout = new HorizontalLayout(new H4("Datum: "), new Span(vacancy.getVeroeffentlichungsdatum().toString()));
        HorizontalLayout locationLayout = new HorizontalLayout(new H4("Standort: "), new Span(vacancy.getStandort()));
        HorizontalLayout infoLayout = new HorizontalLayout(dateLayout, locationLayout);

        H4 description = new H4("Beschreibung: ");
        Div desParagraph = new Div();
        desParagraph.getElement().setProperty(INNER_HTML, markdownConverter.convertToHtml(vacancy.getBeschreibung()));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button bewerben = new Button("Jetzt bewerben");
        bewerben.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bewerben.addClickListener(e -> {
            openApplyDialog(vacancy);
            dialog.close();
        });

        Button closeButton = new Button("Schließen", event -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        buttonLayout.add(bewerben, closeButton);

        dialogLayout.add(title, avatar, type, infoLayout, description, desParagraph, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openApplyDialog(JobPostDTO jobPost) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeight("400px");

        VerticalLayout dialogLayout = new VerticalLayout();
        H4 title = new H4("Bewerbung als: " + jobPost.getTitel());
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        MemoryBuffer cvBuffer = new MemoryBuffer();
        Upload cvUpload = new Upload(cvBuffer);
        cvUpload.setAcceptedFileTypes("application/pdf");
        cvUpload.setMaxFiles(1);
        cvUpload.setVisible(true);
        cvUpload.setDropLabel(new Span("Lebenslauf hier ablegen"));
        Text cvFileNameDisplay = new Text("");

        // Cover Letter Upload
        MemoryBuffer coverLetterBuffer = new MemoryBuffer();
        Upload coverLetterUpload = new Upload(coverLetterBuffer);
        coverLetterUpload.setAcceptedFileTypes("application/pdf");
        coverLetterUpload.setMaxFiles(1);
        coverLetterUpload.setVisible(true);
        coverLetterUpload.setDropLabel(new Span("Anschreiben hier ablegen"));
        Text coverLetterFileNameDisplay = new Text("");

        AtomicBoolean cvUploaded = new AtomicBoolean(false);
        AtomicBoolean coverLetterUploaded = new AtomicBoolean(false);

        // CV Upload Listeners
        cvUpload.addSucceededListener(event -> {
            cvUploaded.set(true);
            cvFileNameDisplay.setText("Hochgeladen: " + event.getFileName());
        });
        cvUpload.addFileRejectedListener(event -> {
            cvUploaded.set(false);
            cvFileNameDisplay.setText("Fehler beim Upload");
            Notification.show("Nur PDF-Dateien sind erlaubt", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        // Cover Letter Upload Listeners
        coverLetterUpload.addSucceededListener(event -> {
            coverLetterUploaded.set(true);
            coverLetterFileNameDisplay.setText("Hochgeladen: " + event.getFileName());
        });
        coverLetterUpload.addFileRejectedListener(event -> {
            coverLetterUploaded.set(false);
            coverLetterFileNameDisplay.setText("Fehler beim Upload");
            Notification.show("Nur PDF-Dateien sind erlaubt", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        Button applyButton = new Button("Bewerben", event -> {

            // Validate that both CV and cover letter are uploaded
            if (!cvUploaded.get() || !coverLetterUploaded.get()) {
                Notification.show("Bitte laden Sie sowohl Lebenslauf als auch Anschreiben als PDF-Dateien hoch",
                                5000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                // CV
                try (InputStream inputStream = cvBuffer.getInputStream()) {
                    byte[] bytes = inputStream.readAllBytes();
                    String base64CV = Base64.getEncoder().encodeToString(bytes);

                    // Cover Letter
                    try (InputStream coverLetterInputStream = coverLetterBuffer.getInputStream()) {
                        byte[] coverLetterBytes = coverLetterInputStream.readAllBytes();
                        String base64CoverLetter = Base64.getEncoder().encodeToString(coverLetterBytes);

                        bewerbungService.saveBewerbung(entityFactory.createBewerbung(jobPost.getJobPost(),
                            sessionService.getCurrentStudent().getStudent(), base64CoverLetter, base64CV));
                        Notification.show("Bewerbung erfolgreich eingereicht", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        dialog.close();
                }
                }
            } catch (Exception e) {
                Notification.show("Fehler beim Hochladen der Dokumente: " + e.getMessage(),
                                5000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        dialogLayout.add(title, coverLetterUpload, coverLetterFileNameDisplay, cvUpload, cvFileNameDisplay, applyButton);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private Div createMarkdownDiv(String title, List<String> items) {
        Div container = new Div();
        container.add(new H3(title));
        items.forEach(item -> {
            Div paragraph = new Div();
            paragraph.getElement().setProperty(INNER_HTML, markdownConverter.convertToHtml(item));
            container.add(paragraph);
        });
        return container;
    }
}
