package com.spirit.application.views.profile.Student;

import com.spirit.application.service.ProfileService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.spirit.application.views.profile.ProfileBaseView;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * View for the student profile page. Inherits from ProfileBaseView.
 * This class handles the display and interaction with the student's profile.
 * @see ProfileBaseView
 */
@Route(value = Globals.Pages.PROFIL_STUDENT, layout = AppView.class)
@RolesAllowed(Globals.Roles.STUDENT)
public class ProfilStudentView extends ProfileBaseView {

    /**
     * Constructs a ProfilStudentView.
     * @param profileService   The service for handling profile-related operations.
     * @param sessionService   The service for session management and authentication.
     */
    public ProfilStudentView(ProfileService profileService, SessionService sessionService) {
        super(profileService, sessionService);
    }
}