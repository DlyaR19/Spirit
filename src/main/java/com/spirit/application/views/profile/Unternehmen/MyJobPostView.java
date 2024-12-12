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

@PageTitle("JobPost Details")
@Route(value = Globals.Pages.MY_JOBPOSTS, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)

public class MyJobPostView extends Composite<VerticalLayout> implements AfterNavigationObserver {

    private static final String INNER_HTML = "innerHTML";
    private final SessionService sessionService;
    private final transient JobPostService jobPostService;
    private final VerticalLayout layout;
    private final transient MarkdownConverter markdownConverter;

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

    public static JobPostDTO getJobPost(JobPost jobPost) {
        return new JobPostDTO(jobPost);
    }

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

    public VerticalLayout createJobPostCards(List<JobPostDTO> jobPosts) {
        VerticalLayout cardsLayout = new VerticalLayout();
        cardsLayout.setWidth("100%");
        cardsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cardsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        for (JobPostDTO jobPost : jobPosts) {

            VerticalLayout card = createJobPostCard(
                    jobPost.getJobPostID(),
                    jobPost.getUnternehmen().getUser().getProfile().getAvatar(),
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

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

        List<JobPostDTO> jobPosts = jobPostService.getJobPostByUnternehmenId(
                        sessionService.getCurrentUnternehmen().getUnternehmenID()
                )
                .stream()
                .map(MyJobPostView::getJobPost)
                .collect(Collectors.toList());
        layout.removeAll();
        layout.add(createJobPostCards(jobPosts));
    }

}
