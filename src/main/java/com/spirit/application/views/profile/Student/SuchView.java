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
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This view allows students to search for job postings based on various criteria,
 * such as job type and search text (e.g., location, description, etc.).
 * It displays a list of job postings and provides the option to view more details
 * and apply for a position.
 */
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

    /**
     * Constructor for initializing the search view.
     * It fetches all available job posts and updates the list displayed on the page.
     * @param jobPostService   service to interact with job posts
     * @param sessionService   service for session management
     * @param bewerbungService service for managing applications
     */
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

        // Dynamische Werte für die Filter
        List<String> employmentTypes = jobPostService.getUniqueEmploymentTypes();
        List<String> locationTypes = jobPostService.getUniqueLocations();
        List<String> companyNameTypes = jobPostService.getUniqueCompanyNameTypes();
        List<String> jobTitleTypes = jobPostService.getUniqueJobTitleTypes();

        // Sortieren der Listen
        employmentTypes.sort(String::compareToIgnoreCase);
        locationTypes.sort(String::compareToIgnoreCase);
        companyNameTypes.sort(String::compareToIgnoreCase);
        jobTitleTypes.sort(String::compareToIgnoreCase);

        HorizontalLayout searchBarLayout = searchbar(employmentTypes, locationTypes, companyNameTypes, jobTitleTypes);

        getContent().getStyle().setAlignItems(Style.AlignItems.CENTER);
        getContent().add(searchBarLayout, layout);
    }

    /**
     * Creates the search bar for the students which includes a text search field and filter selections.
     * @param employmentTypes  the list of unique employment types.
     * @param locationTypes    the list of unique location types.
     * @param companyNameTypes the list of unique company names.
     * @param jobTitleTypes    the list of unique job titles.
     * @return A horizontal layout containing the search bar with filters.
     */
    public HorizontalLayout searchbar(List<String> employmentTypes, List<String> locationTypes, List<String> companyNameTypes, List<String> jobTitleTypes) {
        VerticalLayout searchBarContainer = new VerticalLayout();
        searchBarContainer.setWidth("100%");
        searchBarContainer.setSpacing(false);
        searchBarContainer.getStyle().set("align-items", "stretch");

        HorizontalLayout topRow = new HorizontalLayout();
        topRow.setWidth("100%");

        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("Standort | Beschreibung | ...");
        searchTextField.setWidth("100%");

        Button searchButton = new Button("Suche");
        searchButton.setWidth("150px");

        Button clearSearch = new Button("Zurücksetzen");
        clearSearch.setWidth("150px");

        topRow.add(searchTextField, searchButton, clearSearch);
        topRow.setFlexGrow(1, searchTextField);

        HorizontalLayout bottomRow = new HorizontalLayout();
        bottomRow.setWidth("100%");
        bottomRow.getStyle().set("flex-wrap", "wrap");

        MultiSelectComboBox<String> employmentType = new MultiSelectComboBox<>("Anstellungsart");
        employmentType.setPlaceholder("Teilzeit | Vollzeit | ...");
        employmentType.setWidth("100%");
        employmentType.setItems(employmentTypes);

        MultiSelectComboBox<String> locationType = new MultiSelectComboBox<>("Standort");
        locationType.setPlaceholder("Bonn | Köln | ...");
        locationType.setWidth("100%");
        locationType.setItems(locationTypes);

        MultiSelectComboBox<String> companyNameType = new MultiSelectComboBox<>("Unternehmen");
        companyNameType.setPlaceholder("Telekom | DHL | ...");
        companyNameType.setWidth("100%");
        companyNameType.setItems(companyNameTypes);

        MultiSelectComboBox<String> jobTitleType = new MultiSelectComboBox<>("Jobtitel");
        jobTitleType.setPlaceholder("IT-Security | IT-Support | ...");
        jobTitleType.setWidth("100%");
        jobTitleType.setItems(jobTitleTypes);

        bottomRow.add(employmentType, locationType, companyNameType, jobTitleType);
        bottomRow.setFlexGrow(1, employmentType, locationType, companyNameType, jobTitleType);

        searchBarContainer.add(topRow, bottomRow);

        searchButton.addClickListener(event -> {
            String searchText = searchTextField.getValue();
            Set<String> selectedEmploymentTypes = employmentType.getSelectedItems();
            Set<String> selectedLocationTypes = locationType.getSelectedItems();
            Set<String> selectedCompanyNameTypes = companyNameType.getSelectedItems();
            Set<String> selectedJobTitleTypes = jobTitleType.getSelectedItems();
            performSearch(searchText, selectedEmploymentTypes, selectedLocationTypes, selectedCompanyNameTypes, selectedJobTitleTypes);
        });

        clearSearch.addClickListener(event -> {
            searchTextField.clear();
            employmentType.clear();
            locationType.clear();
            companyNameType.clear();
            jobTitleType.clear();

            searchButton.click();
        });

        return new HorizontalLayout(searchBarContainer);
    }

    /**
     * Executes the search using the provided filters and search text.
     * @param searchText       the text to search in the job posts.
     * @param employmentTypes  the selected employment types.
     * @param locationTypes    the selected location types.
     * @param companyNameTypes the selected company names.
     * @param jobTitleTypes    the selected job titles.
     */
    private void performSearch(String searchText, Set<String> employmentTypes, Set<String> locationTypes, Set<String> companyNameTypes, Set<String> jobTitleTypes) {
        List<JobPostDTO> searchedJobPosts = jobPosts.stream()
                .filter(jobPost -> (employmentTypes.isEmpty() || employmentTypes.contains(jobPost.getAnstellungsart())) &&
                        (locationTypes.isEmpty() || locationTypes.contains(jobPost.getStandort())) &&
                        (companyNameTypes.isEmpty() || companyNameTypes.contains(jobPost.getUnternehmen().getName())) &&
                        (jobTitleTypes.isEmpty() || jobTitleTypes.contains(jobPost.getTitel())) &&
                        (searchText == null || searchText.isEmpty() || jobPostMatchesSearchText(jobPost, searchText)))
                .toList();

        updateJobPostList(searchedJobPosts);
    }

    /**
     * Checks if a job post matches the provided search text.
     * @param jobPost    the job post to check.
     * @param searchText the search text to compare against.
     * @return true if the job post matches the search text, false otherwise.
     */
    private boolean jobPostMatchesSearchText(JobPostDTO jobPost, String searchText) {
        return jobPost.getTitel().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getBeschreibung().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getUnternehmen().getName().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getStandort().toLowerCase().contains(searchText.toLowerCase());
    }

    /**
     * Updates the displayed job posts by refreshing the layout with the provided job posts.
     * @param jobPostToDisplay the list of job posts to display.
     */
    private void updateJobPostList(List<JobPostDTO> jobPostToDisplay) {
        layout.removeAll();
        jobPostToDisplay.forEach(jobPostDTO -> layout.add(createCard(jobPostDTO)));
    }

    /**
     * Creates a card layout for a job post.
     * The card contains job details like the title, company, job type, location, and description.
     * @param jobPost the job post to create the card for
     * @return the card layout for the job post
     */
    public VerticalLayout createCard(JobPostDTO jobPost) {
        VerticalLayout cardLayout = new VerticalLayout();
        Avatar avatar = new Avatar();
        avatar.setImage("data:image/jpeg;base64," + jobPost.getUnternehmen().getUser().getProfil().getAvatar());
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
                jobPost.getUnternehmen().getUser().getProfil().getProfileDescription())
        );
        VerticalLayout contactLayout = new VerticalLayout();
        contactLayout.add(createContactLayout("Email: ", jobPost.getUnternehmen().getUser().getEmail()));
        contactLayout.add(createContactLayout("Webseite: ", jobPost.getUnternehmen().getUser().getProfil().getWebseite()));
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

    /**
     * Creates a horizontal layout containing contact information for the company.
     * @param labelText the label text (e.g., "Email")
     * @param valueText the contact value (e.g., "example@company.com")
     * @return the horizontal layout with contact information
     */
    private HorizontalLayout createContactLayout(String labelText, String valueText) {
        Span label = new Span(labelText);
        label.getStyle().set(FONT_WEIGHT, "bold");
        return new HorizontalLayout(label, new Span(valueText));
    }

    /**
     * Opens a dialog displaying more details about the selected job post.
     * The dialog allows the student to apply for the position by uploading their CV and cover letter.
     * @param jobPost the selected job post
     */
    private void openDialog(JobPostDTO jobPost) {
        jobPostService.incrementViewCount(jobPost.getJobPost());
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        Avatar avatar = new Avatar();
        avatar.setImage("data:image/jpeg;base64," + jobPost.getUnternehmen().getUser().getProfil().getAvatar());
        H2 title = new H2(jobPost.getTitel());
        Button type = new Button(jobPost.getAnstellungsart());
        type.setWidth("min-content");
        type.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        type.setEnabled(true);

        HorizontalLayout dateLayout = new HorizontalLayout(new H4("Datum: "), new Span(jobPost.getVeroeffentlichungsdatum().toString()));
        HorizontalLayout locationLayout = new HorizontalLayout(new H4("Standort: "), new Span(jobPost.getStandort()));
        HorizontalLayout infoLayout = new HorizontalLayout(dateLayout, locationLayout);

        H4 description = new H4("Beschreibung: ");
        Div desParagraph = new Div();
        desParagraph.getElement().setProperty(INNER_HTML, markdownConverter.convertToHtml(jobPost.getBeschreibung()));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button bewerben = new Button("Jetzt bewerben");
        bewerben.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bewerben.addClickListener(e -> {
            openApplyDialog(jobPost);
            dialog.close();
        });

        Button closeButton = new Button("Schließen", event -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        buttonLayout.add(bewerben, closeButton);

        dialogLayout.add(title, avatar, type, infoLayout, description, desParagraph, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * Opens a dialog for the student to apply for a job post.
     * The dialog allows the student to upload their CV and cover letter.
     * @param jobPost the job post to apply for
     */
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
        Button closeButton = new Button("Abbrechen", event -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(applyButton, closeButton);
        dialogLayout.add(title, coverLetterUpload, coverLetterFileNameDisplay, cvUpload, cvFileNameDisplay, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * Creates a div containing a title and a list of items.
     * Each item is displayed as a paragraph with markdown formatting.
     * @param title the title of the div
     * @param items the list of items to display
     * @return the div containing the title and items
     */
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
