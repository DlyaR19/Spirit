package com.spirit.application.views.main;

import com.spirit.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@PageTitle("Über Uns")
@Route(value = "about", layout = MainLayout.class)
@Menu(order = 3, icon = "line-awesome/svg/file.svg")
@AnonymousAllowed
@CssImport("./themes/spirit/views/AboutUsView.css")

public class AboutUsView extends VerticalLayout {

    public AboutUsView() {
        setSpacing(false);
        setPadding(true);
        getStyle().set("background-color", "#f9f9f9").set("padding", "20px");

        // Header
        H1 title = new H1("Willkommen bei Spirit!");
        title.addClassName("centered-title");

        // Einleitung
        Div intro = new Div();
        intro.getStyle()
                .set("text-align", "center")
                .set("max-width", "800px")
                .set("margin", "20px auto");
        intro.add(new Paragraph("Wir bei Spirit glauben daran, dass wir Menschen zusammenbringen können, um großartige Dinge zu erreichen. "
                + "Unsere Plattform verbindet Talente mit den richtigen Möglichkeiten und hilft dabei, die Welt ein Stück besser zu machen. "
                + "Unser Hauptsitz befindet sich im Herzen von Sankt Augustin, einem Ort, an dem Innovation und Kreativität zusammenfließen."));

        // Vision
        H2 visionTitle = new H2("Unsere Vision");
        styleCenterHeading(visionTitle);


        Div vision = new Div();
        vision.addClassName("section");
        vision.add(new Paragraph("Wir möchten die Art und Weise, wie Menschen arbeiten und sich vernetzen, revolutionieren. Unser Ziel ist es, "
                + "eine Plattform zu schaffen, die Menschen nicht nur miteinander verbindet, sondern auch ihre individuellen Stärken und Potenziale hervorhebt."));

        // Mission
        H2 missionTitle = new H2("Unsere Mission");
        styleCenterHeading(missionTitle);

        Div mission = new Div();
        mission.getStyle()
                .set("text-align", "center")
                .set("max-width", "800px")
                .set("margin", "20px auto");
        mission.add(new Paragraph("Bei Spirit stehen die Menschen im Mittelpunkt. Unsere Mission ist es, die besten Technologien mit einem "
                + "menschlichen Ansatz zu kombinieren, um Unternehmen und Individuen gleichermaßen zu helfen, ihre Ziele zu erreichen."));

        // Logo
        H2 logoTitle = new H2("Unser Logo");
        styleCenterHeading(logoTitle);

        Image logo = new Image("./spirit_logo.png", "Spirit Logo");
        logo.setWidth("300px");
        logo.getStyle().set("margin", "20px auto").set("display", "block");

        // Werte
        H2 valuesTitle = new H2("Unsere Werte");
        styleCenterHeading(valuesTitle);

        HorizontalLayout valuesLayout = new HorizontalLayout();
        valuesLayout.setWidthFull();
        valuesLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        valuesLayout.setSpacing(true);

        valuesLayout.add(
                createValueTile(VaadinIcon.LIGHTBULB, "Innovation", "Wir sind immer auf der Suche nach neuen Wegen, um Probleme zu lösen."),
                createValueTile(VaadinIcon.GROUP, "Teamarbeit", "Gemeinsam erreichen wir mehr als allein."),
                createValueTile(VaadinIcon.TOOLS, "Verantwortung", "Wir übernehmen Verantwortung für unsere Plattform und deren Auswirkungen."),
                createValueTile(VaadinIcon.TROPHY, "Qualität", "Unser Ziel ist es, die höchsten Standards zu erfüllen.")
        );

        // Team
        H2 teamTitle = new H2("Unser Team");
        styleCenterHeading(teamTitle);

        Div team = new Div();
        team.getStyle()
                .set("text-align", "center")
                .set("max-width", "800px")
                .set("margin", "20px auto");
        team.add(
                new Paragraph("Hinter Spirit steht ein leidenschaftliches Team, das hart daran arbeitet, unsere Vision Wirklichkeit werden zu lassen."),
                new H3("Kevin Finkelmeyer"),
                new Paragraph("UI und Product Owner Proxy – Experte für nutzerfreundliche Designs und intuitive Benutzeroberflächen."),
                new H3("Axel Mitzel"),
                new Paragraph("Usability-Experte – Verantwortlich für die kontinuierliche Verbesserung der Nutzererfahrung."),
                new H3("Dlyar Kanat"),
                new Paragraph("Programmierer – Testet und optimiert den Code auf höchste Standards."),
                new H3("Rokash Ramouk Bakki"),
                new Paragraph("Software-Architektin – Verantwortlich für robuste und skalierbare Softwarelösungen."),
                new H3("Toka Bahloul"),
                new Paragraph("Datenbank-Expertin – Sicherstellung effizienter Datenstrukturen und -zugriffe."),
                new H3("Waldemar Prokofjew"),
                new Paragraph("Usability-Experte – Unterstützt bei der Gestaltung benutzerfreundlicher Anwendungen."),
                new H3("Shkodran Faniq"),
                new Paragraph("Programmierer – Entwickelt und optimiert unsere Softwarelösungen mit Präzision."),
                new H3("Ben Mbaye"),
                new Paragraph("Quality Manager – Gewährleistet die höchste Qualität in unseren Produkten."),
                new H3("Tasnim Hanasoglu"),
                new Paragraph("Scrum Masterin und Teamleiterin – Leitet das Team mit klarer Vision und organisiert den agilen Prozess."),
                new H3("Altina Kastrati"),
                new Paragraph("Prozess-Managerin – Optimiert Arbeitsabläufe und fördert eine reibungslose Zusammenarbeit."),
                new H3("Zerda Aydin"),
                new Paragraph("Prozess-Managerin – Unterstützt bei der Qualitätssicherung und dem Testen der Anwendung.")
        );

        // Footer
        Div footer = new Div();
        footer.addClassName("footer");
        footer.add(
                new Paragraph("© 2024 Spirit. Alle Rechte vorbehalten."),
                new Paragraph("Hauptsitz: Sankt Augustin - Wo Kreativität und Innovation zusammenfließen."),
                new Paragraph("Kontakt: info@spirit.com | Tel: +49 123 456 789")
        );

        // Elemente hinzufügen
        add(title, intro, visionTitle, vision, missionTitle, mission, logoTitle, logo, valuesTitle, valuesLayout, teamTitle, team, footer);
    }

    private void styleCenterHeading(H2 heading) {
        heading.getStyle()
                .set("text-align", "center")
                .set("font-weight", "bold")
                .set("font-style", "italic")
                .set("margin-bottom", "10px");
    }
    private void styleCenterHeading(H1 heading) {
        heading.getStyle()
                .set("text-align", "center")
                .set("font-weight", "bold")
                .set("font-style", "italic")
                .set("margin-bottom", "10px");
    }

    private VerticalLayout createValueTile(VaadinIcon icon, String title, String description) {
        Icon valueIcon = icon.create();
        valueIcon.setSize("32px");
        valueIcon.getStyle().set("color", "var(--lumo-primary-color)");

        H3 valueTitle = new H3(title);
        valueTitle.getStyle().set("font-weight", "bold").set("font-style", "italic");

        Div valueDescription = new Div(new Text(description));
        valueDescription.getStyle().set("text-align", "center");

        VerticalLayout tile = new VerticalLayout(valueIcon, valueTitle, valueDescription);
        tile.setPadding(true);
        tile.setAlignItems(FlexComponent.Alignment.CENTER);
        tile.getStyle()
                .set("background", "var(--lumo-shade-5pct)")
                .set("border-radius", "8px")
                .set("padding", "16px")
                .set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");

        return tile;
    }
}
