package com.spirit.application.views.main;

import com.spirit.application.views.dashboard.DashboardView;
import com.spirit.application.views.login.LoginView;
import com.spirit.application.views.register.RegisterView;
import com.spirit.application.views.main.AboutUsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Spirit")
public class MainView extends AppLayout {

    public MainView() {
        // Setup Navbar und Sidebar
        createNavbar();
        createDrawer();
    }

    private void createNavbar() {
        // Toggle-Button für die Sidebar
        DrawerToggle toggle = new DrawerToggle();

        // Navbar-Titel
        Span navbarTitle = new Span("Spirit");
        navbarTitle.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "20px")
                .set("margin-left", "1rem");

        // App-Logo rechts
        Image logo = new Image("/images/spirit_logo.png", "Spirit Logo");
        logo.setHeight("40px");
        logo.getStyle()
                .set("margin-left", "auto")
                .set("margin-right", "1rem");

        // Navbar Layout
        HorizontalLayout navbar = new HorizontalLayout(toggle, navbarTitle, logo);
        navbar.setWidthFull();
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.getStyle()
                .set("background-color", "#ffffff")
                .set("padding", "0.5rem 1rem")
                .set("border-bottom", "1px solid #ddd");

        addToNavbar(navbar);
    }

    private void createDrawer() {
        // Sidebar Layout
        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.setSizeFull();
        drawerLayout.setPadding(false);
        drawerLayout.setSpacing(false);

        // App-Titel in der Sidebar
        Span appTitle = new Span("Spirit");
        appTitle.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "20px")
                .set("margin", "1rem");

        // Navigationselemente hinzufügen
        VerticalLayout navMenu = new VerticalLayout();
        navMenu.add(createNavItem("Dashboard", VaadinIcon.DASHBOARD, DashboardView.class));
        navMenu.add(createNavItem("Login", VaadinIcon.SIGN_IN, LoginView.class));
        navMenu.add(createNavItem("Register", VaadinIcon.USER, RegisterView.class));
        navMenu.add(createNavItem("About Spirit", VaadinIcon.INFO_CIRCLE, AboutUsView.class));

        drawerLayout.add(appTitle, navMenu);
        addToDrawer(drawerLayout);
    }

    private RouterLink createNavItem(String label, VaadinIcon icon, Class<? extends Component> route) {
        Icon navIcon = icon.create();
        navIcon.setSize("20px");

        Span navLabel = new Span(label);
        navLabel.getStyle().set("margin-left", "10px");

        HorizontalLayout layout = new HorizontalLayout(navIcon, navLabel);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        RouterLink link = new RouterLink();
        link.add(layout);
        link.setRoute(route); // Klassenreferenz für die Route
        link.getStyle().set("text-decoration", "none").set("color", "inherit");

        return link;
    }
}