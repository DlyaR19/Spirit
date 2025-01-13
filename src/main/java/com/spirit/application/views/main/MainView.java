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

/**
 * The main view of the application that handles the user role-based redirection.
 * The `beforeEnter` method checks the role of the currently authenticated user and forwards to the appropriate profile view.
 */
@Route(Globals.Pages.MAIN)
@PermitAll
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    /**
     * This method is called before the view is entered. It checks the role of the authenticated user and
     * redirects to the corresponding profile page based on the user's role.
     * @param event The event that is triggered before the view is entered.
     *              It contains information about the navigation and the route.
     */
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

