package com.spirit.application.views;

import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.util.Utils;
import com.spirit.application.views.profile.Student.MyApplicationView;
import com.spirit.application.views.profile.Student.ProfilStudentView;
import com.spirit.application.views.profile.Student.SearchView;
import com.spirit.application.views.profile.Unternehmen.AddJobPostView;
import com.spirit.application.views.profile.Unternehmen.MyJobPostView;
import com.spirit.application.views.profile.Unternehmen.ProfileUnternehmenView;
import com.spirit.application.views.profile.Unternehmen.ShowApplicationView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.SlotUtils;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

@Route(Globals.Pages.APP)
public class AppView extends AppLayout {

    private final transient SessionService sessionService;

    @Autowired
    public AppView(SessionService sessionService) {
        this.sessionService = sessionService;
        setUpUI();
    }

    private static Tab createTab(String s, Class<? extends Component> navigationTarget) {
        final Tab t = new Tab();
        t.add(new RouterLink(s, navigationTarget));
        ComponentUtil.setData(t, Class.class, navigationTarget);
        return t;
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
        viewTitle.setWidthFull();
        layout.add(viewTitle);

        HorizontalLayout topRightLayout = new HorizontalLayout();
        topRightLayout.setWidthFull();
        topRightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topRightLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Abmelden", e -> logoutUser());
        topRightLayout.add(menuBar);

        layout.add(topRightLayout);
        return layout;
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
            tabs = Utils.append(tabs, createTab("Profil", ProfilStudentView.class));
            tabs = Utils.append(tabs, createTab("Job suchen", SearchView.class));
            tabs = Utils.append(tabs, createTab("Meine Bewerbungen", MyApplicationView.class));
            //tabs = Utils.append(tabs, createTab("Passwort ändern", UpdatePasswordView.class));
        } else if (sessionService.getUserRole().contains(Globals.Roles.UNTERNEHMEN)) {
            tabs = Utils.append(tabs, createTab("Profil", ProfileUnternehmenView.class));
            //tabs = Utils.append(tabs, createTab("Passwort ändern", UpdatePasswordView.class));
            tabs = Utils.append(tabs, createTab("Stellenausschreibung hinzufügen", AddJobPostView.class));
            tabs = Utils.append(tabs, createTab("Meine Stellenausschreibungen", MyJobPostView.class));
            tabs = Utils.append(tabs, createTab("Bewerbungen einsehen", ShowApplicationView.class));
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
}
