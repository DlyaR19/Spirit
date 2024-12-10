package com.spirit.application.service;

import com.spirit.application.dto.StudentDTO;
import com.spirit.application.dto.UnternehmenDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

/**
 * SessionService - Verwaltet Benutzersitzungen in der Anwendung
 * Verantwortlichkeiten:
 * - Beenden der aktuellen Benutzersitzung
 * - Abrufen des aktuellen Benutzers
 * - Bereitstellen von Benutzerdaten und Rollen
 */

@Service
public class SessionService {

    // Dependency Injection des SecurityService
    private final SecurityService securityService;

    // Konstruktor zur Initialisierung des SecurityService
    public SessionService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Beendet die aktuelle Benutzersitzung
     * - Leitet zur Login-Seite weiter
     * - Invalidiert die aktuelle Session
     * - Schließt die Vaadin-Sitzung
     */
    public void endSession() {
        // Weiterleitung zur Login-Seite
        UI.getCurrent().getPage().setLocation(Globals.Pages.LOGIN);
        // Invalidierung der aktuellen HTTP-Session
        VaadinSession.getCurrent().getSession().invalidate();

        VaadinSession.getCurrent().close();
    }

    /**
     * Holt den aktuellen Benutzer aus der Vaadin-Sitzung
     * @return UserDTO des aktuellen Benutzers
     */
    public UserDTO getCurrentUser() {
        return (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
    }

    /**
     * Holt den aktuellen Studenten
     * Cast des aktuellen Benutzers zu StudentDTO
     * @return StudentDTO des aktuellen Benutzers
     */
    public StudentDTO getCurrentStudent() {
        return (StudentDTO) getCurrentUser();
    }

    /**
     * Holt das aktuelle Unternehmen
     * Cast des aktuellen Benutzers zu UnternehmenDTO
     * @return UnternehmenDTO des aktuellen Benutzers
     */
    public UnternehmenDTO getCurrentUnternehmen() {
        return (UnternehmenDTO) getCurrentUser();
    }

    /**
     * Ermittelt die Rolle des aktuellen Benutzers
     * @return Rollen-String des Benutzers
     */
    public String getUserRole() {
        return securityService.loadUserByUsername(getCurrentUser().getUsername())
                .getAuthorities().toString();
    }
}
