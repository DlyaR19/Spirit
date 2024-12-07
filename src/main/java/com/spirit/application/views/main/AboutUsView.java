package com.spirit.application.views.main;


import com.spirit.application.util.Globals;
import com.spirit.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;


@PageTitle("About Spirit")
@Route(value = Globals.Pages.ABOUTUS, layout = MainLayout.class)
@Menu(order = 3)
@AnonymousAllowed
@PermitAll
public class AboutUsView extends VerticalLayout {

    // TODO Besser gestalten (erst nach dem einloggen wird das Logo angezeigt FIX!!)

    public AboutUsView() {
        setSpacing(true);
        setPadding(true);
        setAlignItems(FlexComponent.Alignment.CENTER);

        //Titel
        H1 title = new H1("Willkommen bei Spirit");
        title.getElement().getStyle().set("text-align", "center");

        //Einleitung
        Div intro = new Div();
        intro.setText("Wir bei Spirit glauben daran, dass wir Menschen zusammenbringen können, um großartige Dinge zu erreichen.\n" +
                "                        Unsere Plattform verbindet Talente mit den richtigen Möglichkeiten und hilft dabei, die Welt ein Stück\n" +
                "                        besser zu machen. Unser Hauptsitz befindet sich im Herzen von Sankt Augustin, einem Ort, an dem Innovation\n" +
                "                        und Kreativität zusammenfließen.\n");

        //Vision
        H2 visionTitle = new H2("Unsere Vision");
        visionTitle.getElement().getStyle().set("text-align", "center");
        Div vision = new Div();
        vision.setText("Wir möchten die Art und Weise, wie Menschen arbeiten und sich vernetzen, revolutionieren. Unser Ziel ist es,\n" +
                "                        eine Plattform zu schaffen, die Menschen nicht nur miteinander verbindet, sondern auch ihre individuellen\n" +
                "                        Stärken und Potenziale hervorhebt.\n");

        //Mission
        H2 missionTitle = new H2("Unsere Mission");
        missionTitle.getElement().getStyle().set("text-align", "center");
        Div mission = new Div();
        mission.setText("Bei Spirit stehen die Menschen im Mittelpunkt. Unsere Mission ist es, die besten Technologien mit einem\n" +
                "                        menschlichen Ansatz zu kombinieren, um Unternehmen und Individuen gleichermaßen zu helfen, ihre Ziele zu erreichen.\n");


        //Logo
        H2 logoTitle = new H2("Unser Logo");
        logoTitle.getElement().getStyle().set("text-align", "center");
        Image logo = new Image("/images/spirit_logo.png", "Spirit Logo");
        logo.setWidth("300px");

        //Werte
        H2 valuesTitle = new H2("Unsere Werte");
        valuesTitle.getElement().getStyle().set("text-align", "center");
        Div valuesList = new Div();
        valuesList.add(new Text("Innovation: Wir sind immer auf der Suche nach neuen Wegen, um Probleme zu lösen."));
        valuesList.add(new Text("Teamarbeit: Gemeinsam erreichen wir mehr als allein."));
        valuesList.add(new Text("Verantwortung: Wir übernehmen Verantwortung für unsere Plattform und deren Auswirkungen."));
        valuesList.add(new Text("Qualität: Unser Ziel ist es, die höchsten Standards zu erfüllen."));

        //Team
        H2 teamTitle = new H2("Unser Team");
        teamTitle.getElement().getStyle().set("text-align", "center");
        Div team = new Div();
        team.add(new Text("Hinter Spirit steht ein leidenschaftliches Team, das hart daran arbeitet, unsere Vision Wirklichkeit werden zu lassen."));

        //Teammitglieder
        team.add(new Div(new Text("Kevin Finkelmeyer – UI und Product Owner Proxy")));
        team.add(new Div(new Text("Axel Mitzel – Usability-Experte")));
        team.add(new Div(new Text("Dlyar Kanat – Programmierer")));
        team.add(new Div(new Text("Rokash Ramouk Bakki – Software-Architektin")));
        team.add(new Div(new Text("Toka Bahloul – Datenbank-Expertin")));
        team.add(new Div(new Text("Waldemar Prokofjew – Usability-Experte")));
        team.add(new Div(new Text("Shkodran Faniq – Programmierer")));
        team.add(new Div(new Text("Ben Mbaye – Quality Manager")));
        team.add(new Div(new Text("Tasnim Hanasoglu – Scrum Masterin und Teamleiterin")));
        team.add(new Div(new Text("Altina Kastrati – Prozess-Managerin")));
        team.add(new Div(new Text("Zerda Aydin – Prozess-Managerin")));

        // Hinzufügen der Komponenten zum Layout
        add(title, intro, visionTitle, vision, missionTitle, mission, logoTitle, logo,
                valuesTitle, valuesList, teamTitle, team);
    }
}
