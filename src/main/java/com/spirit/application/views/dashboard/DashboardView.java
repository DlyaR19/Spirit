package com.spirit.application.views.dashboard;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.spirit.application.views.main.MainView;

import java.util.Arrays;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainView.class)
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(FlexComponent.Alignment.CENTER);

        H1 title = new H1("Stellenanzeigen");
        title.getStyle().set("font-size", "2rem").set("margin-bottom", "1rem");
        add(title);

        // Unternehmensdaten
        List<Company> companies = Arrays.asList(
                new Company("Telekom", "Führendes Telekommunikationsunternehmen, das innovative Lösungen bietet.",
                        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMwAAADACAMAAAB/Pny7AAAAeFBMVEX..."),
                new Company("Brunata", "Spezialisiert auf Energie- und Ressourcenmanagement.",
                        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMwAAADACAMAAAB/Pny7AAAArlBMVEX..."),
                new Company("IBM", "Globales Unternehmen, bekannt für Technologie und IT-Lösungen.",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/IBM_logo.svg/1200px-IBM_logo.svg.png"),
                new Company("Apple", "Marktführer in Innovation und Technologie.",
                        "https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg")
        );

        // Unternehmensgitter
        Div companyGrid = new Div();
        companyGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(250px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%");

        for (Company company : companies) {
            companyGrid.add(createCompanyCard(company));
        }

        add(companyGrid);
    }

    /**
     * Erstellt eine Unternehmenskarte mit Logo, Name und Beschreibung.
     *
     * @param company Die Unternehmensdaten
     * @return Ein Div-Element mit der Unternehmenskarte
     */
    private Div createCompanyCard(Company company) {
        Div card = new Div();
        card.getStyle()
                .set("border", "1px solid #ddd")
                .set("border-radius", "12px")
                .set("padding", "1rem")
                .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                .set("text-align", "center")
                .set("background-color", "#ffffff");

        // Unternehmenslogo
        Image logo = new Image(company.getImage(), company.getCompanyName() + " logo");
        logo.setWidth("100px");
        logo.getStyle().set("margin-bottom", "1rem");

        // Unternehmensname
        H2 companyName = new H2(company.getCompanyName());
        companyName.getStyle()
                .set("font-size", "1.5rem")
                .set("margin", "0.5rem 0");

        // Unternehmensbeschreibung
        Paragraph description = new Paragraph(company.getDescription());
        description.getStyle()
                .set("font-size", "0.9rem")
                .set("color", "#555");

        card.add(logo, companyName, description);
        return card;
    }

    /**
     * Hilfsklasse für Unternehmensdaten.
     */
    private static class Company {
        private final String companyName;
        private final String description;
        private final String image;

        public Company(String companyName, String description, String image) {
            this.companyName = companyName;
            this.description = description;
            this.image = image;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getDescription() {
            return description;
        }

        public String getImage() {
            return image;
        }
    }
}