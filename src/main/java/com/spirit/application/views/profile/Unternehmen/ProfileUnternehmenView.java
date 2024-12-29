package com.spirit.application.views.profile.Unternehmen;

import com.spirit.application.service.ProfileService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.spirit.application.views.MainLayout;
import com.spirit.application.views.profile.ProfileBaseView;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * View for displaying and editing the profile of the currently logged-in company user.
 * This view is accessible only to users with the "UNTERNEHMEN" role.
 * <p>This class extends `ProfileBaseView` to reuse shared functionality for profile management.</p>
 */
@Route(value = Globals.Pages.PROFIL_UNTERNEHMEN, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)
public class ProfileUnternehmenView extends ProfileBaseView {

    /**
     * Constructs the `ProfileUnternehmenView` by initializing the base class with the required services.
     * @param profileService Service for managing profile-related operations.
     * @param sessionService Service for managing user session data.
     */
    public ProfileUnternehmenView(ProfileService profileService, SessionService sessionService) {
        super(profileService, sessionService);
    }
}