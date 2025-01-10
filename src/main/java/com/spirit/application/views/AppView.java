package com.spirit.application.views;

import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.Utils;
import com.spirit.application.views.profile.ChatListView;
import com.spirit.application.views.profile.ChatView;
import com.spirit.application.views.profile.ProfilSucheView;
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
 * Main view of the application extending {@link AppLayout}. It sets up the UI with a side navigation menu,
 * a header with a logout button, and a footer. The view content changes dynamically based on the user role
 * (either Student or Company).
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @Route}: Specifies the route URL for this view, mapped to {@link Globals.Pages#APP}.</li>
 *   <li>{@code @CssImport}: Imports custom CSS for styling the view components.</li>
 * </ul>
 */
@CssImport("./themes/spirit/views/AppView.css")
@Route(Globals.Pages.APP)
public class AppView extends AppLayout {

    // SessionService für Benutzersitzungsverwaltung
    private final transient SessionService sessionService;

    /**
     * Constructor initializes the view with the provided {@link SessionService}.
     * @param sessionService the service for managing user sessions
     */
    @Autowired
    public AppView(SessionService sessionService) {
        this.sessionService = sessionService;
        setUpUI();
    }

    /**
     * Creates a {@link Tab} for the navigation menu with an icon and text.
     * @param s the text for the tab
     * @param icon the icon for the tab
     * @param navigationTarget the class of the component to navigate to
     * @return the created {@link Tab}
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
     * Sets up the user interface by configuring the layout components.
     */
    private void setUpUI() {
        Tabs sideMenu;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        sideMenu = createMenu();
        addToDrawer(createDrawerContent(sideMenu));
    }

    /**
     * Creates the header content, including a toggle button and title, and a logout button.
     * @return the header component
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
     * Creates the logout button layout.
     * @return the layout with the logout button
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
     * Creates the navigation menu.
     * @return the tabs element containing the menu items
     */
    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.add(createMenuItems());
        return tabs;
    }

    /**
     * Creates the menu items based on the user's role.
     * @return an array of menu items (tabs)
     */
    private Component[] createMenuItems() {

        Tab[] tabs = new Tab[]{};

        if (sessionService.getUserRole().contains(Globals.Roles.STUDENT)) {
            tabs = Utils.append(tabs, createTab("Profil", VaadinIcon.USER, ProfilStudentView.class));
            tabs = Utils.append(tabs, createTab("Job suchen", VaadinIcon.SEARCH, SuchView.class));
            tabs = Utils.append(tabs, createTab("Meine Bewerbungen", VaadinIcon.FILE_TEXT, MyBewerbungView.class));
            tabs = Utils.append(tabs, createTab("Chat List", VaadinIcon.CHAT, ChatListView.class));
            //tabs = Utils.append(tabs, createTab("Passwort ändern", UpdatePasswordView.class));
            tabs = Utils.append(tabs, createTab("Profil suchen", VaadinIcon.SEARCH, ProfilSucheView.class));
        } else if (sessionService.getUserRole().contains(Globals.Roles.UNTERNEHMEN)) {
            tabs = Utils.append(tabs, createTab("Profil", VaadinIcon.BUILDING, ProfileUnternehmenView.class));
            //tabs = Utils.append(tabs, createTab("Passwort ändern", UpdatePasswordView.class));
            tabs = Utils.append(tabs, createTab("Stellenausschreibung hinzufügen", VaadinIcon.PLUS_CIRCLE, AddJobPostView.class));
            tabs = Utils.append(tabs, createTab("Meine Stellenausschreibungen", VaadinIcon.CLIPBOARD, MyJobPostView.class));
            tabs = Utils.append(tabs, createTab("Bewerbungen einsehen", VaadinIcon.FILE_PROCESS, ShowBewerbungView.class));
            tabs = Utils.append(tabs, createTab("Chat List", VaadinIcon.CHAT, ChatListView.class));
            tabs = Utils.append(tabs, createTab("Profil suchen", VaadinIcon.SEARCH, ProfilSucheView.class));
        }
        return tabs;
    }

    /**
     * Logs out the user by ending the session.
     */
    private void logoutUser() {
        sessionService.endSession();
    }

    /**
     * Adds components to the navbar.
     * @param touchOptimized whether the view is optimized for touch
     * @param components the components to add
     */
    @Override
    public void addToNavbar(boolean touchOptimized, Component... components) {
        String slot = "navbar" + (touchOptimized ? " touch-optimized" : "");
        SlotUtils.addToSlot(this, slot, components);
    }

    /**
     * Adds components to the drawer.
     * @param components the components to add
     */
    @Override
    public void addToDrawer(Component... components) {
        SlotUtils.addToSlot(this, "drawer", components);
    }

    /**
     * Creates the drawer content with the provided menu.
     * @param menu the menu to display in the drawer
     * @return the drawer content component
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        verticalLayout.add(menu);
        return verticalLayout;
    }

    /**
     * Creates the footer component.
     * @return the footer element
     */
    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassName(LumoUtility.Padding.Vertical.SMALL);
        footer.add(new Hr());
        footer.add(new Span("© 2021 Spirit"));
        return footer;
    }

}
