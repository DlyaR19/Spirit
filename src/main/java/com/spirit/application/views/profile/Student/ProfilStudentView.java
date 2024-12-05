package com.spirit.application.views.profile.Student;

import com.spirit.application.service.ProfileService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.AppView;
import com.spirit.application.views.MainLayout;
import com.spirit.application.views.profile.ProfileBaseView;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = Globals.Pages.PROFIL_STUDENT, layout = AppView.class)
@RolesAllowed(Globals.Roles.STUDENT)
public class ProfilStudentView extends ProfileBaseView {


    public ProfilStudentView(ProfileService profileService, SessionService sessionService) {
        super(profileService, sessionService);
    }
}