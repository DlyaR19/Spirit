package com.spirit.application.views.main;


import com.spirit.application.util.Globals;
import com.spirit.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

/**
 * View that displays information about the Spirit company, including its vision, mission, values, team, and contact information.
 * This view is publicly accessible to all users and can be accessed through the "Über Uns" page.
 */
@PageTitle("Über Uns")
@Route(value = Globals.Pages.ABOUTUS, layout = MainLayout.class)
@Menu(order = 3)
@AnonymousAllowed
@PermitAll
public class AboutUsView extends VerticalLayout {

    /**
     * Constructor for initializing the "Über Uns" page view.
     * Sets up the layout with title, introduction, vision, mission, company values, team information, and footer.
     */
    public AboutUsView() {
        setWidthFull();
        setSpacing(true);
        setPadding(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        getStyle().set("display", "flex").set("flex-direction", "column").set("min-height", "100vh");

        //Titel
        H1 title = new H1("Willkommen bei Spirit!");
        title.getElement().getStyle().set("text-align", "center");
        title.getElement().getStyle().set("font-style", "italic").set("text-decoration", "underline");

        //Einleitung
        Div intro = createTextBlock("Wir bei Spirit glauben daran, dass wir Menschen zusammenbringen können, um großartige Dinge zu erreichen. " +
                        "Unsere Plattform verbindet Talente mit den richtigen Möglichkeiten und hilft dabei, die Welt ein Stück besser zu machen. " +
                        "Unser Hauptsitz befindet sich im Herzen von Sankt Augustin, einem Ort, an dem Innovation und Kreativität zusammenfließen.",
                "left");

        //Vision
        H2 visionTitle = createSectionTitle("Unsere Vision");
        Div vision = createTextBlock("Wir möchten die Art und Weise, wie Menschen arbeiten und sich vernetzen, revolutionieren. " +
                        "Unser Ziel ist es, eine Plattform zu schaffen, die Menschen nicht nur miteinander verbindet, sondern auch ihre individuellen Stärken und Potenziale hervorhebt.",
                "left");

        //Mission
        H2 missionTitle = createSectionTitle("Unsere Mission");
        Div mission = createTextBlock("Bei Spirit stehen die Menschen im Mittelpunkt. Unsere Mission ist es, die besten Technologien " +
                        "mit einem menschlichen Ansatz zu kombinieren, um Unternehmen und Individuen gleichermaßen zu helfen, ihre Ziele zu erreichen.",
                "left");

        //Logo
        H2 logoTitle = createSectionTitle("Unser Logo");
        Image logo = new Image("https://i.ibb.co/YWqXFWz/Erstelle-ein-Logo-mit-Titel-Spirit-f-r-mein-Software-Engineering-Projekt.jpg", "Spirit Logo");
        logo.setWidth("300px");

        //Werte
        H2 valuesTitle = createSectionTitle("Unsere Werte");
        Div valuesSection = createValuesSection();

        //Team
        H2 teamTitle = createSectionTitle("Unser Team");
        Div team = createTeamSection();

        // Footer
        Div footer = createFooter();

        // Hinzufügen der Komponenten zum Layout
        add(title, intro, visionTitle, vision, missionTitle, mission, logoTitle, logo, valuesTitle, valuesSection, teamTitle, team, footer);
    }

    /**
     * Creates a section title as an H2 element.
     * @param title The title of the section.
     * @return The H2 element with the section title.
     */
    private H2 createSectionTitle(String title) {
        H2 sectionTitle = new H2(title);
        sectionTitle.getElement().getStyle().set("text-align", "center");
        return sectionTitle;
    }

    /**
     * Creates a text block as a Div element.
     * @param text The text content of the block.
     * @param textAlign The alignment of the text.
     * @return The Div element with the text block.
     */
    private Div createTextBlock(String text, String textAlign) {
        Div textBlock = new Div();
        textBlock.setText(text);
        textBlock.getStyle().set("text-align", textAlign).set("max-width", "800px").set("margin", "20px auto");
        return textBlock;
    }

    /**
     * Creates a section with tiles for the company values.
     * @return The Div element with the values section.
     */
    private Div createValuesSection() {
        Div valuesSection = new Div();
        valuesSection.getStyle()
                .set("display", "flex")
                .set("flex-wrap", "wrap")
                .set("justify-content", "center")
                .set("gap", "20px")
                .set("margin", "20px auto")
                .set("max-width", "800px");

        // Kacheln für Werte
        valuesSection.add(
                createValueTile("Innovation",
                        "Wir sind immer auf der Suche nach neuen Wegen, um Probleme zu lösen.",
                        VaadinIcon.LIGHTBULB),
                createValueTile("Teamarbeit",
                        "Gemeinsam erreichen wir mehr als allein.",
                        VaadinIcon.USERS),
                createValueTile("Verantwortung",
                        "Wir übernehmen Verantwortung für unsere Plattform und deren Auswirkungen.",
                        VaadinIcon.SHIELD),
                createValueTile("Qualität",
                        "Unser Ziel ist es, die höchsten Standards zu erfüllen.",
                        VaadinIcon.TROPHY),
                createValueTile("Nachhaltigkeit",
                        "Wir setzen uns für umweltbewusste und nachhaltige Praktiken ein.",
                        VaadinIcon.ARCHIVE),
                createValueTile("Kundenorientierung",
                        "Unsere Kunden stehen im Mittelpunkt all unserer Entscheidungen.",
                        VaadinIcon.HANDSHAKE)
        );

        return valuesSection;
    }

    /**
     * Creates a tile for a company value.
     * @param title The title of the value.
     * @param description The description of the value.
     * @param icon The icon for the value.
     * @return The Div element with the value tile.
     */
    private Div createValueTile(String title, String description, VaadinIcon icon) {
        Div tile = new Div();
        tile.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("padding", "15px")
                .set("border", "1px solid #ddd")
                .set("border-radius", "8px")
                .set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)")
                .set("width", "180px")
                .set("text-align", "center");

        Icon valueIcon = icon.create();
        valueIcon.setSize("40px");
        valueIcon.getStyle().set("color", "#007bff").set("margin-bottom", "10px");

        H3 titleElement = new H3(title);
        titleElement.getStyle().set("margin", "5px 0");

        Paragraph descriptionElement = new Paragraph(description);
        descriptionElement.getStyle().set("font-size", "14px").set("color", "#555");

        tile.add(valueIcon, titleElement, descriptionElement);
        return tile;
    }

    /**
     * Creates a section with information about the team members.
     * @return The Div element with the team section.
     */
    private Div createTeamSection() {
        Div team = new Div();
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
        return team;
    }

    /**
     * Creates a footer with contact information.
     * @return The Div element with the footer.
     */
    private Div createFooter() {
        Div footer = new Div();
        footer.addClassName("footer");
        footer.add(
                new Paragraph("© 2024 Spirit. Alle Rechte vorbehalten."),
                new Paragraph("Hauptsitz: Sankt Augustin"),
                new Paragraph("- Wo Kreativität und Innovation zusammenfließen -"),
                new Paragraph("Kontakt: info@spirit.com | Tel: +49 123 456 789")
        );
        footer.getStyle()
                .set("background-color", "#f4f4f4")
                .set("padding", "20px")
                .set("text-align", "center")
                .set("width", "100%");
        return footer;
    }
}
