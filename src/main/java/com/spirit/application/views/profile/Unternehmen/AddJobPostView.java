package com.spirit.application.views.profile.Unternehmen;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.service.JobPostService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.EntityFactory;
import com.spirit.application.util.Globals;
import com.spirit.application.util.MarkdownConverter;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
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

@PageTitle("Add JobPost")
@Route(value = Globals.Pages.JOBPOST, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)
public class AddJobPostView extends Composite<VerticalLayout> {

    private final transient EntityFactory entityFactory;
    private final transient SessionService sessionService;
    private final transient JobPostService jobPostService;


    private static final String DELETE = "Löschen";

    @Autowired
    public AddJobPostView(EntityFactory entityFactory, SessionService sessionService, JobPostService jobPostService, MarkdownConverter markdownConverter) {
        this.entityFactory = entityFactory;
        this.sessionService = sessionService;
        this.jobPostService = jobPostService;
        setUpUI();
    }

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
            JobPost jobPost = entityFactory.createJobPost(comboBox.getValue(), title.getValue(), location.getValue(), textArea.getValue(), sessionService.getCurrentUnternehmen().getUnternehmen(), Date.valueOf(LocalDate.now()));
            jobPostService.saveJobPost(jobPost);
            comboBox.clear();
            title.clear();
            location.clear();
            textArea.clear();
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
