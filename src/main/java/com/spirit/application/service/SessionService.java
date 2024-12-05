package com.spirit.application.service;

import com.spirit.application.dto.StudentDTO;
import com.spirit.application.dto.UnternehmenDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final SecurityService securityService;

    public SessionService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void endSession() {
        UI.getCurrent().getPage().setLocation(Globals.Pages.LOGIN);
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }

    public UserDTO getCurrentUser() {
        return (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
    }

    public StudentDTO getCurrentStudent() {
        return (StudentDTO) getCurrentUser();
    }

    public UnternehmenDTO getCurrentUnternehmen() {
        return (UnternehmenDTO) getCurrentUser();
    }

    public String getUserRole() {
        return securityService.loadUserByUsername(getCurrentUser().getUsername())
                .getAuthorities().toString();
    }
}
