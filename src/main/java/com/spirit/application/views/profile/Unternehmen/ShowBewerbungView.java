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

    public VerticalLayout studentCard(BewerbungDTO bewerbung, JobPostDTO jobPost) {
        VerticalLayout studentCardLayout = new VerticalLayout();
        HorizontalLayout jobPostInfo = new HorizontalLayout();
        jobPostInfo.add(new H4(jobPost.getTitle()));
        HorizontalLayout avaterLayout = new HorizontalLayout();
        Avatar studentAvatar = new Avatar();
        studentAvatar.setImage(
                "data:image/jpeg;base64," + bewerbung.getStudent().getUser().getProfile().getAvatar()
        );
        Button type = new Button(jobPost.getEmploymentType());
        type.setWidth("min-content");
        type.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        type.setEnabled(true);
        H4 studentName = new H4(bewerbung.getStudent().getFirstName() + " " + bewerbung.getStudent().getLastName());
        avaterLayout.add(studentAvatar, studentName);
        H6 profileDescription = new H6("Profilbeschreibung");
        Div profileDescriptionParagraph = new Div();
        profileDescriptionParagraph.getElement().setProperty(
                INNER_HTML, markdownConverter.convertToHtml(
                        bewerbung.getStudent().getUser().getProfile().getProfileDescription())
        );
        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button showProfileButton = new Button("Profil anzeigen");
        showProfileButton.addClickListener(e -> {
            Dialog profileDialog = new Dialog();
            profileDialog.setWidth("600px");
            VerticalLayout profileLayout = new VerticalLayout();
            Avatar studentAvatarDialog = new Avatar();
            studentAvatarDialog.setImage(
                    "data:image/jpeg;base64," + bewerbung.getStudent().getUser().getProfile().getAvatar()
            );
            H6 profileName = new H6(bewerbung.getStudent().getFirstName() + " " + bewerbung.getStudent().getLastName());
            // TODO get Interessen und co from profilStudentLayout
            TextArea profileDescriptionDialog = new TextArea("Profilbeschreibung");
            profileDescriptionDialog.setValue(bewerbung.getStudent().getUser().getProfile().getProfileDescription());
            profileDescriptionDialog.setReadOnly(true);
            profileLayout.add(studentAvatarDialog, profileName, profileDescriptionDialog);
            profileDialog.add(profileLayout);
            profileDialog.open();


        });

        Button downloadAnschreibenButton = getDownloadAnschreibenButton(bewerbung);


        buttonLayout.add(showProfileButton, downloadAnschreibenButton);


        studentCardLayout.add(jobPostInfo, type, avaterLayout, profileDescription, profileDescriptionParagraph, buttonLayout);
        studentCardLayout.setWidth("100%");
        studentCardLayout.setMaxWidth("700px");
        studentCardLayout.getStyle().set("border", "1px solid #ccc");
        studentCardLayout.getStyle().set("border-radius", "8px");
        studentCardLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        return studentCardLayout;
    }

    private @NotNull Button getDownloadAnschreibenButton(BewerbungDTO bewerbung) {
        Button downloadAnschreibenButton = new Button("Download Bewerbung");
        downloadAnschreibenButton.addClickListener(e -> {
            String base64coverLetter = bewerbungService.getAnschreiben(bewerbung.getBewerbungID());
            if (base64coverLetter != null && !base64coverLetter.isEmpty()) {

                byte[] pdfBytes = Base64.getDecoder().decode(base64coverLetter);
                StreamResource resource = new StreamResource("Bewerbung.pdf", () -> new ByteArrayInputStream(pdfBytes));
                resource.setContentType("application/pdf");

                Anchor downloadLink = new Anchor(resource, "Download");
                downloadLink.getElement().setAttribute("download", true);
                downloadLink.getStyle().set("display", "none");
                getContent().add(downloadLink);

                downloadLink.getElement().executeJs("this.click();");

                Notification.show("Bewerbung-Download gestartet.");
            } else {
                Notification.show("Keine Bewerbung gefunden.");
                downloadAnschreibenButton.setEnabled(false);
            }
        });
        return downloadAnschreibenButton;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        loadAndDisplayBerwerbung(sessionService.getCurrentUnternehmen().getUnternehmenID());
    }

    private void loadAndDisplayBerwerbung(Long unternehmenId) {
        List<JobPost> jobPosts = jobPostService.getJobPostByUnternehmenId(unternehmenId);
        layout.getStyle().setAlignItems(Style.AlignItems.CENTER);
        for (JobPost jobPost : jobPosts) {
            JobPostDTO jobPostDTO = new JobPostDTO(jobPost);
            List<Bewerbung> bewerbungs = bewerbungService.getAllBewerbung(jobPost.getJobPostID());
            for (Bewerbung bewerbung : bewerbungs) {
                BewerbungDTO bewerbungDTO = new BewerbungDTO(bewerbung);
                bewerbungList.add(bewerbungDTO);
                layout.add(studentCard(bewerbungDTO, jobPostDTO));
            }
        }
    }
}
