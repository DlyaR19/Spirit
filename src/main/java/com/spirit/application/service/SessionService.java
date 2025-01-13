package com.spirit.application.service;

import com.spirit.application.dto.StudentDTO;
import com.spirit.application.dto.UnternehmenDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user sessions.
 * Includes methods for starting and ending sessions, as well as retrieving user information.
 */
@Service
public class SessionService {

    private final SecurityService securityService;

    public SessionService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Ends the current user session.
     */
    public void endSession() {
        UI.getCurrent().getPage().setLocation(Globals.Pages.LOGIN);
        VaadinSession.getCurrent().getSession().invalidate();

        VaadinSession.getCurrent().close();
    }

    /**
     * Retrieves the currently logged-in user.
     * @return the current user as UserDTO
     */
    public UserDTO getCurrentUser() {
        return (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
    }

    /**
     * Retrieves the currently logged-in student.
     * @return the current student as StudentDTO
     */
    public StudentDTO getCurrentStudent() {
        return (StudentDTO) getCurrentUser();
    }

    /**
     * Retrieves the currently logged-in company.
     * @return the current company as UnternehmenDTO
     */
    public UnternehmenDTO getCurrentUnternehmen() {
        return (UnternehmenDTO) getCurrentUser();
    }

    /**
     * Retrieves the role of the currently logged-in user.
     * @return the user's role
     */
    public String getUserRole() {
        return securityService.loadUserByUsername(getCurrentUser().getUsername())
                .getAuthorities().toString();
    }
}
