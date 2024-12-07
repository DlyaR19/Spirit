package com.spirit.application.views.profile.Unternehmen;

import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

// TODO Chat view for Unternehmen
@PageTitle("Chat")
@Route(value = Globals.Pages.CHAT_UNTERNEHMEN, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)
public class UnternehmenChatView extends VerticalLayout {

    public UnternehmenChatView() {
        addClassName("unternehmen-chat-view");
    }
}
