package com.spirit.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.List;

/**
 * The main layout for the application, extending {@link AppLayout}. This layout is used as the
 * foundation of the application UI, with a drawer, header, and footer. It supports navigation
 * between different views and dynamically updates the title based on the current view.
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @Layout}: Marks this class as the main layout for the application.</li>
 *   <li>{@code @AnonymousAllowed}: Allows access to this layout without authentication.</li>
 * </ul>
 */
@Layout
@AnonymousAllowed // Erlaubt Zugriff ohne Authentifizierung
public class MainLayout extends AppLayout {

    private H1 viewTitle; // Titel der aktuellen Ansicht

    /**
     * Constructor initializes the layout with a drawer and a header.
     */
    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    /**
     * Creates the header with a toggle button and title.
     */
    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    /**
     * Creates the drawer with menu items and a footer.
     */
    private void addDrawerContent() {
        Image logo = new Image("https://i.ibb.co/YWqXFWz/Erstelle-ein-Logo-mit-Titel-Spirit-f-r-mein-Software-Engineering-Projekt.jpg", "Spirit Logo"); // Use your actual logo URL here
        logo.setWidth("40px"); // Adjust the size of the logo as needed
        logo.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Padding.Horizontal.SMALL); // Adjust spacing
        logo.getStyle().set("cursor", "pointer");
        logo.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("/"));
        });

        Span appName = new Span("Spirit");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        // Combine the logo and appName in the header
        HorizontalLayout headerLayout = new HorizontalLayout(logo, appName);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.addClassNames(LumoUtility.Padding.NONE);

        Header header = new Header(headerLayout);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    /**
     * Creates the navigation structure with menu items.
     * @return a {@link SideNav} component with the menu items.
     */
    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            Icon icon = getIconForMenuEntry(entry.title());
            SideNavItem item = icon != null
                    ? new SideNavItem(entry.title(), entry.path(), icon)
                    : new SideNavItem(entry.title(), entry.path());
            nav.addItem(item);
        });

        return nav;
    }

    /**
     * Returns the appropriate icon for a menu entry based on its title.
     * @param title the title of the menu entry.
     * @return the corresponding {@link Icon} for the menu entry.
     */
    private Icon getIconForMenuEntry(String title) {
        // Hier die passenden Icons für die Menüpunkte definieren
        return switch (title.toLowerCase()) {
            case "dashboard" -> VaadinIcon.HOME.create();
            case "login" -> VaadinIcon.SIGN_IN.create();
            case "registrierung" -> VaadinIcon.USER.create();
            case "über uns" -> VaadinIcon.INFO_CIRCLE.create();
            default -> null; // Kein Icon für unbekannte Menüpunkte
        };
    }

    /**
     * Creates the footer with a copyright notice.
     * @return a {@link Footer} component with the copyright notice.
     */
    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames(LumoUtility.Padding.Vertical.SMALL);
        layout.add(new Hr());
        layout.add(new Span("© 2024 Spirit"));
        return layout;
    }

    /**
     * Sets the title of the current view.
     */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    /**
     * Returns the title of the current view.
     * @return the title of the current view.
     */
    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}