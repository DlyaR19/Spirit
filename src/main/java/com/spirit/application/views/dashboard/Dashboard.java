package com.spirit.application.views.dashboard;

import com.spirit.application.dto.JobPostDTO;
import com.spirit.application.service.JobPostService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Route(value = Globals.Pages.DASHBOARD, layout = MainLayout.class)
@Menu(order = 0)
@AnonymousAllowed
@PermitAll
public class Dashboard extends Composite<VerticalLayout> {

    private static final String FONT_WEIGHT = "font-weight";
    private static final String INNER_HTML = "innerHTML";

    private final VerticalLayout layout;
    private final List<JobPostDTO> jobPosts = new ArrayList<>();
    private final transient MarkdownConverter markdownConverter = new MarkdownConverter();
    private final transient JobPostService jobPostService;

    @Autowired
    public Dashboard(JobPostService jobPostService) {
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

    public HorizontalLayout searchbar(List<String> employmentTypes, List<String> locationTypes, List<String> companyNameTypes, List<String> jobTitleTypes) {
        // Haupt-Container für die Suchleiste
        VerticalLayout searchBarContainer = new VerticalLayout();
        searchBarContainer.setWidth("100%");
        searchBarContainer.setSpacing(false);
        searchBarContainer.getStyle().set("align-items", "stretch");

        // Obere Zeile mit Textfeld und Buttons
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

        // Untere Zeile mit Filtern
        HorizontalLayout bottomRow = new HorizontalLayout();
        bottomRow.setWidth("1200px");

        MultiSelectComboBox<String> employmentType = new MultiSelectComboBox<>("Anstellungsart");
        employmentType.setPlaceholder("Teilzeit | Vollzeit | ...");
        employmentType.setWidth("300px");
        employmentType.setItems(employmentTypes);

        MultiSelectComboBox<String> locationType = new MultiSelectComboBox<>("Standort");
        locationType.setPlaceholder("Bonn | Köln | ...");
        locationType.setWidth("300px");
        locationType.setItems(locationTypes);

        MultiSelectComboBox<String> companyNameType = new MultiSelectComboBox<>("Unternehmen");
        companyNameType.setPlaceholder("Telekom | DHL | ...");
        companyNameType.setWidth("300px");
        companyNameType.setItems(companyNameTypes);

        MultiSelectComboBox<String> jobTitleType = new MultiSelectComboBox<>("Jobtitel");
        jobTitleType.setPlaceholder("IT-Security | IT-Support | ...");
        jobTitleType.setWidth("300px");
        jobTitleType.setItems(jobTitleTypes);

        bottomRow.add(employmentType, locationType, companyNameType, jobTitleType);
        bottomRow.setFlexGrow(1, employmentType, locationType, companyNameType, jobTitleType);

        // Container füllen
        searchBarContainer.add(topRow, bottomRow);

        // Such-Button Logik
        searchButton.addClickListener(event -> {
            String searchText = searchTextField.getValue();
            Set<String> selectedEmploymentTypes = employmentType.getSelectedItems();
            Set<String> selectedLocationTypes = locationType.getSelectedItems();
            Set<String> selectedCompanyNameTypes = companyNameType.getSelectedItems();
            Set<String> selectedJobTitleTypes = jobTitleType.getSelectedItems();
            performSearch(searchText, selectedEmploymentTypes, selectedLocationTypes, selectedCompanyNameTypes, selectedJobTitleTypes);
        });

        // Clear-Button Logik
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

    private boolean jobPostMatchesSearchText(JobPostDTO jobPost, String searchText) {
        return jobPost.getTitel().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getBeschreibung().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getUnternehmen().getName().toLowerCase().contains(searchText.toLowerCase()) ||
                jobPost.getStandort().toLowerCase().contains(searchText.toLowerCase());
    }

    private void updateJobPostList(List<JobPostDTO> jobPostToDisplay) {
        layout.removeAll();
        jobPostToDisplay.forEach(jobPostDTO -> layout.add(createCard(jobPostDTO)));
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
            Notification.show("Jetzt einloggen und bewerben!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            UI.getCurrent().navigate(Globals.Pages.LOGIN);
            dialog.close();
        });

        Button closeButton = new Button("Schließen", event -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        buttonLayout.add(bewerben, closeButton);

        dialogLayout.add(title, avatar, type, infoLayout, description, desParagraph, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }
}
