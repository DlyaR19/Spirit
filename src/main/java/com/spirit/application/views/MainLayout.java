package com.spirit.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
 * Navigation bevor der Anmeldung
 *
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("Spirit");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

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

    private Icon getIconForMenuEntry(String title) {
        // Hier die passenden Icons für die Menüpunkte definieren
        switch (title.toLowerCase()) {
            case "dashboard":
                return VaadinIcon.HOME.create();
            case "login":
                return VaadinIcon.SIGN_IN.create();
            case "register":
                return VaadinIcon.USER.create();
            case "über uns":
                return VaadinIcon.INFO_CIRCLE.create();
            default:
                return null; // Kein Icon für unbekannte Menüpunkte
        }
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames(LumoUtility.Padding.Vertical.SMALL);
        layout.add(new Hr());
        layout.add(new Span("© 2024 Spirit"));
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}