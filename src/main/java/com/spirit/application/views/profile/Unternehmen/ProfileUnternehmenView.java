package com.spirit.application.views.profile.Unternehmen;

import com.spirit.application.service.ProfileService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.spirit.application.views.MainLayout;
import com.spirit.application.views.profile.ProfileBaseView;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

// TODO anstelle von webseite vielleicht Textfelder dynamisch generieren (siehe ProfileBaseView)
// TODO andere Informationen hinzufügen wie z.B. Branche, Ansprechpartner, etc.

@Route(value = Globals.Pages.PROFIL_UNTERNEHMEN, layout = AppView.class)
@RolesAllowed(Globals.Roles.UNTERNEHMEN)
public class ProfileUnternehmenView extends ProfileBaseView {

    public ProfileUnternehmenView(ProfileService profileService, SessionService sessionService) {
        super(profileService, sessionService);
    }
}