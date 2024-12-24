package com.spirit.application.views;

import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.Utils;
import com.spirit.application.views.profile.ChatListView;
import com.spirit.application.views.profile.ChatView;
import com.spirit.application.views.profile.Student.MyBewerbungView;
import com.spirit.application.views.profile.Student.ProfilStudentView;
import com.spirit.application.views.profile.Student.SuchView;
import com.spirit.application.views.profile.Unternehmen.AddJobPostView;
import com.spirit.application.views.profile.Unternehmen.MyJobPostView;
import com.spirit.application.views.profile.Unternehmen.ProfileUnternehmenView;
import com.spirit.application.views.profile.Unternehmen.ShowBewerbungView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.SlotUtils;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dies ist die Hauptansicht der Anwendung nach erfolgreicher Anmeldung
 * Erbt von AppLayout, um das grundlegende Layout-Framework von Vaadin zu nutzen
 */


@CssImport("./themes/spirit/views/AppView.css")
@Route(Globals.Pages.APP)
public class AppView extends AppLayout {

    // SessionService für Benutzersitzungsverwaltung
    private final transient SessionService sessionService;

    /**
     * Konstruktor initialisiert die Ansicht
     */
    @Autowired
    public AppView(SessionService sessionService) {
        this.sessionService = sessionService;
        setUpUI();
    }

    /**
     * Erstellt ein Tab-Element für die Navigation mit Icon und Text
     * @param s Text für den Tab
     * @param icon Icon für den Tab
     * @param navigationTarget Zielkomponente für Navigation
     */
    private static Tab createTab(String s, VaadinIcon icon, Class<? extends Component> navigationTarget) {
        // Icon-Setup
        Icon tabIcon = icon.create();
        tabIcon.setSize("24px");
        tabIcon.getStyle().set("margin", "0 auto");

        // Link-Setup
        RouterLink link = new RouterLink(s, navigationTarget);
        link.getStyle().set("text-align", "center");
        link.addClassName("menu-link");

        // Layout für Icon und Text
        HorizontalLayout layout = new HorizontalLayout(tabIcon, link);
        layout.addClassName("menu-item"); // CSS-Klasse für die Linie und Abstände
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER); // Inhalte zentrieren

        // Tab erstellen und Navigationsziel setzen
        final Tab t = new Tab(layout);
        ComponentUtil.setData(t, Class.class, navigationTarget);
        return t;
    }

    /**
     * Initialisiert die Benutzeroberfläche
     */
    private void setUpUI() {
        Tabs sideMenu;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        sideMenu = createMenu();
        addToDrawer(createDrawerContent(sideMenu));
    }

    /**
     * Erstellt den Inhalt des Headers
     * @return Header-Element
     */
    private Component createHeaderContent() {

        H1 viewTitle;
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(new DrawerToggle());
        viewTitle = new H1();
        viewTitle.setWidthFull();
        layout.add(viewTitle);

        HorizontalLayout topRightLayout = new HorizontalLayout();
        topRightLayout.setWidthFull();
        topRightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topRightLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("logout-button");
        menuBar.addItem(createLogoutButton(), e -> logoutUser());
        topRightLayout.add(menuBar);

        layout.add(topRightLayout);
        return layout;
    }

    /**
     * Erstellt den Logout-Button
     * @return Layout mit Logout-Button
     */
    private HorizontalLayout createLogoutButton() {
        Icon icon = VaadinIcon.SIGN_OUT.create();
        icon.setSize("16px");
        icon.getStyle().set("margin-right", "5px");
        Span logoutText = new Span("Abmelden");
        HorizontalLayout layout = new HorizontalLayout(icon, logoutText);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.getStyle().set("cursor", "pointer");
        return layout;
    }

    /**
     * Erstellt das Menü für die Navigation
     * @return Tabs-Element mit Menüpunkten
     */
    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.add(createMenuItems());
        return tabs;
    }

    /**
     * Erstellt die Menüpunkte für die Navigation
     * @return Array mit Menüpunkten
     */
    private Component[] createMenuItems() {

        Tab[] tabs = new Tab[]{};

        if (sessionService.getUserRole().contains(Globals.Roles.STUDENT)) {
            tabs = Utils.append(tabs, createTab("Profil", VaadinIcon.USER, ProfilStudentView.class));
            tabs = Utils.append(tabs, createTab("Job suchen", VaadinIcon.SEARCH, SuchView.class));
            tabs = Utils.append(tabs, createTab("Meine Bewerbungen", VaadinIcon.FILE_TEXT, MyBewerbungView.class));
            tabs = Utils.append(tabs, createTab("Chat List", VaadinIcon.CHAT, ChatListView.class));
            //tabs = Utils.append(tabs, createTab("Passwort ändern", UpdatePasswordView.class));
        } else if (sessionService.getUserRole().contains(Globals.Roles.UNTERNEHMEN)) {
            tabs = Utils.append(tabs, createTab("Profil", VaadinIcon.BUILDING, ProfileUnternehmenView.class));
            //tabs = Utils.append(tabs, createTab("Passwort ändern", UpdatePasswordView.class));
            tabs = Utils.append(tabs, createTab("Stellenausschreibung hinzufügen", VaadinIcon.PLUS_CIRCLE, AddJobPostView.class));
            tabs = Utils.append(tabs, createTab("Meine Stellenausschreibungen", VaadinIcon.CLIPBOARD, MyJobPostView.class));
            tabs = Utils.append(tabs, createTab("Bewerbungen einsehen", VaadinIcon.FILE_PROCESS, ShowBewerbungView.class));
            tabs = Utils.append(tabs, createTab("Chat List", VaadinIcon.CHAT, ChatListView.class));
        }
        return tabs;
    }

    /**
     * Beendet die Benutzersitzung
     */
    private void logoutUser() {
        sessionService.endSession();
    }

    /**
     * Fügt Komponenten zum Navbar hinzu
     * @param touchOptimized Touch-optimierte Ansicht
     * @param components Komponenten
     */
    @Override
    public void addToNavbar(boolean touchOptimized, Component... components) {
        String slot = "navbar" + (touchOptimized ? " touch-optimized" : "");
        SlotUtils.addToSlot(this, slot, components);
    }

    /**
     * Fügt Komponenten zum Drawer hinzu
     * @param components Komponenten
     */
    @Override
    public void addToDrawer(Component... components) {
        SlotUtils.addToSlot(this, "drawer", components);
    }

    /**
     * Erstellt den Inhalt des Drawers
     * @param menu Menü für den Drawer
     * @return Drawer-Inhalt
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        verticalLayout.add(menu);
        return verticalLayout;
    }

    /**
     * Erstellt den Footer
     * @return Footer-Element
     */
    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassName(LumoUtility.Padding.Vertical.SMALL);
        footer.add(new Hr());
        footer.add(new Span("© 2021 Spirit"));
        return footer;
    }

}
