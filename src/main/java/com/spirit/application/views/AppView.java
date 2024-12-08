package com.spirit.application.views;

import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.Utils;
import com.spirit.application.views.profile.Student.MyBewerbungView;
import com.spirit.application.views.profile.Student.ProfilStudentView;
import com.spirit.application.views.profile.Student.SearchView;
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

@CssImport("./themes/spirit/views/AppView.css")
@Route(Globals.Pages.APP)
public class AppView extends AppLayout {

    private final transient SessionService sessionService;

    @Autowired
    public AppView(SessionService sessionService) {
        this.sessionService = sessionService;
        setUpUI();
    }

    private static Tab createTab(String label, VaadinIcon icon, Class<? extends Component> navigationTarget) {
        Icon tabIcon = icon.create();
        tabIcon.setSize("24px"); // Icon-Größe anpassen
        tabIcon.getStyle().set("margin", "0 auto"); // Zentrierung des Icons

        RouterLink link = new RouterLink(label, navigationTarget);
        link.getStyle().set("text-align", "center"); // Text zentrieren
        link.addClassName("menu-link"); // CSS-Klasse hinzufügen

        // Vertikales Layout für Icon und Text
        VerticalLayout layout = new VerticalLayout(tabIcon, link);
        layout.addClassName("menu-item"); // CSS-Klasse für die Linie und Abstände
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER); // Inhalte zentrieren

        final Tab tab = new Tab(layout); // Füge das Layout in den Tab ein
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private void setUpUI() {
        Tabs sideMenu;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        sideMenu = createMenu();
        addToDrawer(createDrawerContent(sideMenu));
    }

    private Component createHeaderContent() {
        H1 viewTitle;
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.addAndExpand(viewTitle); // Titel wird erweitert

        HorizontalLayout topRightLayout = new HorizontalLayout();
        topRightLayout.setWidthFull();
        topRightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topRightLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Abmeldebutton mit Icon
        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("logout-button"); // Optional: CSS-Klasse für Styling
        menuBar.addItem(createLogoutButton(), e -> logoutUser()); // Logout-Button hinzufügen
        topRightLayout.add(menuBar);

        layout.add(topRightLayout);
        return layout;
    }

    private HorizontalLayout createLogoutButton() {
        Icon logoutIcon = VaadinIcon.SIGN_OUT.create(); // Icon für Abmeldung
        logoutIcon.setSize("16px"); // Größe des Icons
        logoutIcon.getStyle().set("margin-right", "5px"); // Abstand zwischen Icon und Text

        Span logoutText = new Span("Abmelden"); // Text für den Button

        // Kombiniere Icon und Text
        HorizontalLayout buttonLayout = new HorizontalLayout(logoutIcon, logoutText);
        buttonLayout.setSpacing(false); // Kein zusätzlicher Abstand
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Inhalte zentrieren
        buttonLayout.getStyle().set("cursor", "pointer"); // Zeige Hand-Cursor bei Hover

        return buttonLayout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        Tab[] tabs = new Tab[]{};

        if (sessionService.getUserRole().contains(Globals.Roles.STUDENT)) {
            tabs = Utils.append(tabs, createTab("Profil", VaadinIcon.USER, ProfilStudentView.class));
            tabs = Utils.append(tabs, createTab("Job suchen", VaadinIcon.SEARCH, SearchView.class));
            tabs = Utils.append(tabs, createTab("Meine Bewerbungen", VaadinIcon.FILE_TEXT, MyBewerbungView.class));
        } else if (sessionService.getUserRole().contains(Globals.Roles.UNTERNEHMEN)) {
            tabs = Utils.append(tabs, createTab("Profil", VaadinIcon.BUILDING, ProfileUnternehmenView.class));
            tabs = Utils.append(tabs, createTab("Stellenausschreibung hinzufügen", VaadinIcon.PLUS_CIRCLE, AddJobPostView.class));
            tabs = Utils.append(tabs, createTab("Meine Stellenausschreibungen", VaadinIcon.CLIPBOARD, MyJobPostView.class));
            tabs = Utils.append(tabs, createTab("Bewerbungen einsehen", VaadinIcon.FILE_PROCESS, ShowBewerbungView.class));
        }

        return tabs;
    }

    private void logoutUser() {
        sessionService.endSession();
    }

    @Override
    public void addToNavbar(boolean touchOptimized, Component... components) {
        String slot = "navbar" + (touchOptimized ? " touch-optimized" : "");
        SlotUtils.addToSlot(this, slot, components);
    }

    @Override
    public void addToDrawer(Component... components) {
        SlotUtils.addToSlot(this, "drawer", components);
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        verticalLayout.add(menu);
        return verticalLayout;
    }
    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames(LumoUtility.Padding.Vertical.SMALL);
        layout.add(new Hr());
        layout.add(new Span("© 2024 Spirit"));
        return layout;
    }
}