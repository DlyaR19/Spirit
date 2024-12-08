package com.spirit.application.views.profile.Student;


import com.spirit.application.dto.BewerbungDTO;
import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.service.BewerbungService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

@Route(value="student/meine-bewerbungen", layout = AppView.class)
@RolesAllowed(Globals.Roles.STUDENT)
public class MyBewerbungView extends Composite<VerticalLayout> {

    private final VerticalLayout layout;
    private final transient BewerbungService bewerbungService;


    public MyBewerbungView(BewerbungService bewerbungService, SessionService sessionService) {
        this.bewerbungService = bewerbungService;
        this.layout = new VerticalLayout();
        this.layout.getStyle().setAlignItems(Style.AlignItems.CENTER);
        getContent().getStyle().setAlignItems(Style.AlignItems.CENTER);
        displayStudentBewerbung(sessionService.getCurrentStudent().getStudentID());
        getContent().add(layout);
    }

    private void displayStudentBewerbung(Long studentId) {
        List<BewerbungDTO> bewerbungList = new ArrayList<>();
        for (Bewerbung bewerbung : bewerbungService.getAllBewerbungByStudent(studentId)) {
            bewerbungList.add(new BewerbungDTO(bewerbung));
        }
        for (BewerbungDTO bewerbung : bewerbungList) {
            layout.add(bewerbungCard(bewerbung));
        }
    }

    public VerticalLayout bewerbungCard(BewerbungDTO bewerbung) {
        VerticalLayout bewerbungCard = new VerticalLayout();
        HorizontalLayout unternehmenLayout = new HorizontalLayout();
        Avatar avatar = new Avatar();
        avatar.setImage(
                "data:image/jpeg;base64," +
                        bewerbung.getJobPost().getUnternehmen().getUser().getProfile().getAvatar()
        );
        unternehmenLayout.add(avatar, new H4(bewerbung.getJobPost().getUnternehmen().getName()));
        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.add(new H3("Bewerbung als: "), new Span(bewerbung.getJobPost().getTitel()));
        VerticalLayout jobPostLayout = new VerticalLayout();
        jobPostLayout.add(
                new H3("Stellenbeschreibung: "),
                new Span(bewerbung.getJobPost().getBeschreibung())
        );
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(new Button("zurückziehen", buttonClickEvent -> openDialog(bewerbung, bewerbungCard)));
        bewerbungCard.add(unternehmenLayout, infoLayout, jobPostLayout, buttonLayout);
        bewerbungCard.setWidth("100%");
        bewerbungCard.setMaxWidth("700px");
        bewerbungCard.getStyle().set("border", "1px solid #ccc");
        bewerbungCard.getStyle().set("border-radius", "8px");
        bewerbungCard.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        return bewerbungCard;
    }

    public void openDialog(BewerbungDTO bewerbung, VerticalLayout bewerbungCard) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setHeight("200px");
        VerticalLayout dialogLayout = new VerticalLayout();
        H5 title = new H5("Möchstest du die Bewerbung zurückziehen?");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(new Button("Ja", buttonClickEvent -> {
            bewerbungService.deleteBewerbung(bewerbung.getBewerbung());
            layout.remove(bewerbungCard);
            dialog.close();
        }), new Button("Abbrechen" , buttonClickEvent -> dialog.close()));
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialogLayout.add(title, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }
}
