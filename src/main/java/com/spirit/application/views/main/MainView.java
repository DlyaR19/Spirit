package com.spirit.application.views.main;

import com.spirit.application.views.profile.Unternehmen.ProfileUnternehmenView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import com.spirit.application.util.Globals;
import com.spirit.application.views.profile.Student.ProfilStudentView;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Route(Globals.Pages.MAIN)
@PermitAll
//@Menu(order = 0)
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    public MainView(){
        // TODO eine Dashboard View hinzufügen mit Stellenausschreibungen

    }

    @Override
    public synchronized void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            switch (role) {
                case "ROLE_" + Globals.Roles.STUDENT -> event.forwardTo(ProfilStudentView.class);
                case "ROLE_" + Globals.Roles.UNTERNEHMEN -> event.forwardTo(ProfileUnternehmenView.class);
                default -> throw new IllegalStateException("Unexpected value: " + role);
            }
        }
    }
}

