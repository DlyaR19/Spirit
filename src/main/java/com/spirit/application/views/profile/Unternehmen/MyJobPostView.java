package com.spirit.application.views.profile.Unternehmen;


import com.spirit.application.dto.JobPostDTO;
import com.spirit.application.entitiy.JobPost;
import com.spirit.application.service.SessionService;
import com.spirit.application.service.JobPostService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * View for displaying and managing the job posts created by the currently logged-in company user.
 * This view is accessible only to users with the "UNTERNEHMEN" role.
 * <p>Users can view their job posts, see details such as title, employment type, location, description,
 * and publish date, and delete job posts if necessary.</p>
 */
@PageTitle("JobPost Details")
@Route(value = Globals.Pages.MY_JOBPOSTS, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)

public class MyJobPostView extends Composite<VerticalLayout> implements AfterNavigationObserver {

    private static final String INNER_HTML = "innerHTML";
    private final SessionService sessionService;
    private final transient JobPostService jobPostService;
    private final VerticalLayout layout;
    private final transient MarkdownConverter markdownConverter;

    /**
     * Constructs the `MyJobPostView` with the necessary services and sets up the main layout.
     * @param sessionService    Service for managing user session data.
     * @param jobPostService    Service for managing job posts.
     * @param markdownConverter Converter for rendering Markdown content into HTML.
     */
    @Autowired
    public MyJobPostView(SessionService sessionService,
                           JobPostService jobPostService,
                           MarkdownConverter markdownConverter) {
        this.sessionService = sessionService;
        this.jobPostService = jobPostService;
        this.markdownConverter = markdownConverter;
        this.layout = new VerticalLayout();
        getContent().add(layout);
    }

    /**
     * Converts a `JobPost` entity into a `JobPostDTO`.
     * @param jobPost The `JobPost` entity to convert.
     * @return A `JobPostDTO` containing the relevant job post details.
     */
    public static JobPostDTO getJobPost(JobPost jobPost) {
        return new JobPostDTO(jobPost);
    }

    /**
     * Creates a card layout to display details of a single job post.
     * @param id              The ID of the job post.
     * @param baseImage       The base64-encoded image of the company's avatar.
     * @param businessName    The name of the company that created the job post.
     * @param titleValue      The title of the job post.
     * @param typeValue       The employment type (e.g., full-time, part-time).
     * @param locationValue   The location of the job post.
     * @param descriptionValue The Markdown-formatted description of the job post.
     * @param publishDate     The date the job post was published.
     * @return A `VerticalLayout` containing the job post card.
     */
    public VerticalLayout createJobPostCard(long id, String baseImage, String businessName, String titleValue,
                                            String typeValue, String locationValue, String descriptionValue,
                                            Date publishDate
    ) {

        Avatar avatar = new Avatar();
        avatar.setImage("data:image/jpeg;base64," + baseImage);
        VerticalLayout cardLayout = new VerticalLayout();
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.add(avatar, new H3(businessName));
        H2 title = new H2(titleValue);
        Button type = new Button(typeValue);
        type.setWidth("min-content");
        type.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        type.setEnabled(true);
        HorizontalLayout dateLayout = new HorizontalLayout(
                new H4("Veröffentlichungsdatum: "),
                new Span(publishDate.toString())
        );
        HorizontalLayout locationLayout = new HorizontalLayout(
                new H4("Standort: "),
                new Span(locationValue)
        );
        HorizontalLayout infoLayout = new HorizontalLayout(dateLayout, locationLayout);
        H4 description = new H4("Beschreibung: ");

        Div desParagraph = new Div();
        desParagraph.getElement().setProperty(INNER_HTML, markdownConverter.convertToHtml(descriptionValue));

        Long viewCount = jobPostService.getViewCount(jobPostService.getJobPostByJobPostID(id));
        Span viewCountSpan = new Span("Aufrufe: " + viewCount);
        viewCountSpan.getStyle().set("font-size", "0.8em");
        viewCountSpan.getStyle().set("color", "gray");

        Button editButton = new Button("Löschen");
        editButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        editButton.addClickListener(event -> {
            jobPostService.deleteJobPost(id);
            Notification.show("Stellenanzeige gelöscht", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(MyJobPostView.class);
        });

        cardLayout.add(titleLayout, title, type, infoLayout, description, desParagraph, viewCountSpan, editButton);

        cardLayout.setWidth("100%");
        cardLayout.setMaxWidth("700px");
        cardLayout.getStyle().set("border", "1px solid #ccc");
        cardLayout.getStyle().set("border-radius", "8px");
        cardLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");

        return cardLayout;
    }

    /**
     * Creates a layout containing all job post cards for the current company.
     * @param jobPosts A list of `JobPostDTO` objects representing the company's job posts.
     * @return A `VerticalLayout` containing all job post cards.
     */
    public VerticalLayout createJobPostCards(List<JobPostDTO> jobPosts) {
        VerticalLayout cardsLayout = new VerticalLayout();
        cardsLayout.setWidth("100%");
        cardsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cardsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        for (JobPostDTO jobPost : jobPosts) {

            VerticalLayout card = createJobPostCard(
                    jobPost.getJobPostID(),
                    jobPost.getUnternehmen().getUser().getProfil().getAvatar(),
                    jobPost.getUnternehmen().getName(),
                    jobPost.getTitel(),
                    jobPost.getAnstellungsart(),
                    jobPost.getStandort(),
                    jobPost.getBeschreibung(),
                    jobPost.getVeroeffentlichungsdatum()
            );
            cardsLayout.add(card);
        }

        return cardsLayout;
    }

    /**
     * Retrieves and displays the job posts created by the currently logged-in company
     * after navigation to this view.
     * @param afterNavigationEvent The navigation event triggering this method.
     */
    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        List<JobPostDTO> jobPosts = jobPostService.getJobPostByUnternehmenId(
                        sessionService.getCurrentUnternehmen().getUnternehmenID()
                )
                .stream()
                .map(MyJobPostView::getJobPost)
                .collect(Collectors.toList());
        if(jobPosts.isEmpty()) {
            VerticalLayout noJobPosts = new VerticalLayout();
            noJobPosts.add(new H4("Keine Stellenanzeigen gefunden."));
            noJobPosts.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.add(noJobPosts);
        } else {
            layout.removeAll();
            layout.add(createJobPostCards(jobPosts));
        }
    }
}
