package com.spirit.application.views.profile.Unternehmen;


import com.spirit.application.dto.BewerbungDTO;
import com.spirit.application.dto.JobPostDTO;
import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.entitiy.JobPost;
import com.spirit.application.service.BewerbungService;
import com.spirit.application.service.JobPostService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * View for displaying applications submitted for the job posts of the currently logged-in company.
 * This view is accessible only to users with the "UNTERNEHMEN" role.
 */
@Route(value = Globals.Pages.SHOW_BEWERBUNG, layout = AppView.class)
@PageTitle("Bewerbungen")
@RolesAllowed(Globals.Roles.UNTERNEHMEN)
public class ShowBewerbungView extends Composite<VerticalLayout> implements AfterNavigationObserver {

    private static final String INNER_HTML = "innerHTML";
    private final VerticalLayout layout = new VerticalLayout();
    private final List<BewerbungDTO> bewerbungList = new ArrayList<>();
    private final transient MarkdownConverter markdownConverter;
    private final transient BewerbungService bewerbungService;
    private final transient SessionService sessionService;
    private final transient JobPostService jobPostService;

    /**
     * Constructs the `ShowBewerbungView` with the required services and initializes the layout.
     * @param markdownConverter Service for converting Markdown to HTML.
     * @param bewerbungService  Service for managing applications.
     * @param sessionService    Service for managing user session data.
     * @param jobPostService    Service for managing job posts.
     */
    public ShowBewerbungView(
            MarkdownConverter markdownConverter,
            BewerbungService bewerbungService,
            SessionService sessionService,
            JobPostService jobPostService
    ) {
        this.markdownConverter = markdownConverter;
        this.bewerbungService = bewerbungService;
        this.sessionService = sessionService;
        this.jobPostService = jobPostService;
        getContent().getStyle().setAlignItems(Style.AlignItems.CENTER);
        getContent().add(layout);
    }

    /**
     * Creates a card layout for displaying information about a student and their application.
     * @param bewerbung The `BewerbungDTO` representing the student's application.
     * @param jobPost   The `JobPostDTO` representing the job post for which the application was submitted.
     * @return A `VerticalLayout` containing the student card.
     */
    public VerticalLayout studentCard(BewerbungDTO bewerbung, JobPostDTO jobPost) {
        VerticalLayout studentCardLayout = new VerticalLayout();

        H4 jobPostTitle = new H4(jobPost.getTitel());
        jobPostTitle.getStyle().set("margin", "0");

        HorizontalLayout jobPostInfo = new HorizontalLayout();
        jobPostInfo.add(jobPostTitle);

        HorizontalLayout avaterLayout = new HorizontalLayout();
        Avatar studentAvatar = new Avatar();
        studentAvatar.setImage(
                "data:image/jpeg;base64," + bewerbung.getStudent().getUser().getProfil().getAvatar()
        );
        Button type = new Button(jobPost.getAnstellungsart());
        type.setWidth("min-content");
        type.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        type.setEnabled(true);
        H4 studentName = new H4(bewerbung.getStudent().getFirstName() + " " + bewerbung.getStudent().getLastName());
        avaterLayout.add(studentAvatar, studentName);
        H6 profileDescription = new H6("Profilbeschreibung");
        Div profileDescriptionParagraph = new Div();
        profileDescriptionParagraph.getElement().setProperty(
                INNER_HTML, markdownConverter.convertToHtml(
                        bewerbung.getStudent().getUser().getProfil().getProfileDescription())
        );
        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button showProfileButton = new Button("Profil anzeigen");
        showProfileButton.addClickListener(e -> {
            Dialog profileDialog = new Dialog();
            profileDialog.setWidth("600px");
            profileDialog.setCloseOnEsc(true);
            profileDialog.setCloseOnOutsideClick(true);

            VerticalLayout profileLayout = new VerticalLayout();
            profileLayout.setSpacing(true);
            profileLayout.setPadding(true);

            // Avatar and Name
            HorizontalLayout avatarNameLayout = new HorizontalLayout();
            avatarNameLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            Avatar avatar = new Avatar();
            avatar.setImage(
                    "data:image/jpeg;base64," + bewerbung.getStudent().getUser().getProfil().getAvatar()
            );

            H3 name = new H3(
                    bewerbung.getStudent().getFirstName() + " " + bewerbung.getStudent().getLastName()
            );
            avatarNameLayout.add(avatar, name);

            // Additional Information
            TextArea birth = new TextArea("Geburtsdatum");
            birth.setValue(String.valueOf(bewerbung.getStudent().getBirthdate()));
            birth.setReadOnly(true);

            TextArea profileDescriptionDialog = new TextArea("Profilbeschreibung");
            profileDescriptionDialog.setValue(
                    bewerbung.getStudent().getUser().getProfil().getProfileDescription()
            );
            profileDescriptionDialog.setReadOnly(true);

            // Add additional sections (e.g., Interests, Skills, etc.)
            Div additionalInfo = new Div();
            additionalInfo.getStyle().set("margin-top", "10px");
            additionalInfo.setText("Interessen und Fähigkeiten:"); // Placeholder for future content

            profileLayout.add(avatarNameLayout, birth, profileDescriptionDialog, additionalInfo);

            profileDialog.add(profileLayout);
            profileDialog.open();
        });

        Button downloadAnschreibenButton = getDownloadAnschreibenButton(bewerbung);
        Button downloadLebenslaufButton = getDownloadLebenslaufButton(bewerbung);
        buttonLayout.add(showProfileButton, downloadLebenslaufButton, downloadAnschreibenButton);

        studentCardLayout.add(jobPostInfo, type, avaterLayout, profileDescription, profileDescriptionParagraph, buttonLayout);
        studentCardLayout.setWidth("100%");
        studentCardLayout.setMaxWidth("700px");
        studentCardLayout.getStyle().set("border", "1px solid #ccc");
        studentCardLayout.getStyle().set("border-radius", "8px");
        studentCardLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        return studentCardLayout;
    }

    /**
     * Creates a button for downloading the cover letter of a student's application.
     * @param bewerbung The `BewerbungDTO` containing the cover letter data.
     * @return A `Button` to initiate the cover letter download.
     */
    private @NotNull Button getDownloadAnschreibenButton(BewerbungDTO bewerbung) {
        Button downloadAnschreibenButton = new Button("Anschreiben herunterladen");
        downloadAnschreibenButton.addClickListener(e -> {
            String base64coverLetter = bewerbungService.getAnschreiben(bewerbung.getBewerbungID());
            if (base64coverLetter != null && !base64coverLetter.isEmpty()) {

                byte[] pdfBytes = Base64.getDecoder().decode(base64coverLetter);
                String filename = String.format("%s_%s_Anschreiben.pdf",
                        bewerbung.getStudent().getLastName(),
                        bewerbung.getStudent().getFirstName()
                );
                StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(pdfBytes));
                resource.setContentType("application/pdf");

                Anchor downloadLink = new Anchor(resource, "Download");
                downloadLink.getElement().setAttribute("download", true);
                downloadLink.getStyle().set("display", "none");
                getContent().add(downloadLink);

                downloadLink.getElement().executeJs("this.click();");

                Notification.show("Anschreiben-Download gestartet.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification.show("Kein Anschreiben gefunden.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        return downloadAnschreibenButton;
    }

    /**
     * Creates a button for downloading the CV of a student's application.
     * @param bewerbung The `BewerbungDTO` containing the CV data.
     * @return A `Button` to initiate the CV download.
     */
    private @NotNull Button getDownloadLebenslaufButton(BewerbungDTO bewerbung) {
        Button downloadLebenslaufButton = new Button("Lebenslauf herunterladen");
        downloadLebenslaufButton.addClickListener(e -> {
            String base64cv = bewerbungService.getLebenslauf(bewerbung.getBewerbungID());
            if (base64cv != null && !base64cv.isEmpty()) {

                byte[] pdfBytes = Base64.getDecoder().decode(base64cv);
                String filename = String.format("%s_%s_Lebenslauf.pdf",
                        bewerbung.getStudent().getLastName(),
                        bewerbung.getStudent().getFirstName()
                );
                StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(pdfBytes));
                resource.setContentType("application/pdf");

                Anchor downloadLink = new Anchor(resource, "Download");
                downloadLink.getElement().setAttribute("download", true);
                downloadLink.getStyle().set("display", "none");
                getContent().add(downloadLink);

                downloadLink.getElement().executeJs("this.click();");

                Notification.show("Lebenslauf-Download gestartet.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification.show("Kein Lebenslauf gefunden.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        return downloadLebenslaufButton;
    }

    /**
     * Triggered after navigation to this view, loads and displays applications
     * for all job posts created by the currently logged-in company.
     * @param event The navigation event triggering this method.
     */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        loadAndDisplayBerwerbung(sessionService.getCurrentUnternehmen().getUnternehmenID());
    }

    /**
     * Loads applications for the company's job posts and updates the layout to display them.
     * @param unternehmenId The ID of the currently logged-in company.
     */
    private void loadAndDisplayBerwerbung(Long unternehmenId) {

        List<JobPost> jobPosts = jobPostService.getJobPostByUnternehmenId(unternehmenId);
        if(jobPosts.isEmpty()) {
            VerticalLayout noJobPosts = new VerticalLayout();
            noJobPosts.add(new H4("Keine Bewerbungen gefunden."));
            noJobPosts.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.add(noJobPosts);
        }
        else {
            layout.getStyle().setAlignItems(Style.AlignItems.CENTER);
            for (JobPost jobPost : jobPosts) {
                JobPostDTO jobPostDTO = new JobPostDTO(jobPost);
                Long bewerbungsAnzahl = bewerbungService.countBewerbungByJobPostId(jobPost.getJobPostID());
                if (bewerbungsAnzahl == 0) {
                    H4 jobPostTitle = new H4(jobPost.getTitel() + " (Keine Bewerbungen)");
                    layout.add(jobPostTitle);
                } else {
                    H4 jobPostTitle = new H4(jobPost.getTitel() + " (" + bewerbungsAnzahl + " Bewerbungen)");
                    layout.add(jobPostTitle);
                }
                List<Bewerbung> bewerbungs = bewerbungService.getAllBewerbung(jobPost.getJobPostID());
                for (Bewerbung bewerbung : bewerbungs) {
                    BewerbungDTO bewerbungDTO = new BewerbungDTO(bewerbung);
                    bewerbungList.add(bewerbungDTO);
                    layout.add(studentCard(bewerbungDTO, jobPostDTO));
                }
            }
        }
    }
}
