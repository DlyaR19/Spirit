package com.spirit.application.views.profile.Student;


import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

// TODO Chat view for students
@PageTitle("Chat")
@Route(value = Globals.Pages.CHAT_STUDENT, layout = AppView.class)
@RolesAllowed(Globals.Roles.STUDENT)
public class StudentChatView extends VerticalLayout {

        public StudentChatView() {
            addClassName("chat");
        }
}
