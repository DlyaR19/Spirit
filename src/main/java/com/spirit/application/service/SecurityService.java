package com.spirit.application.service;


import com.spirit.application.entitiy.User;
import com.spirit.application.repository.StudentRepository;
import com.spirit.application.repository.UnternehmenRepository;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.util.Globals;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SecurityService - Implementiert die Sicherheits- und Authentifizierungslogik
 * Verantwortlichkeiten:
 * - Laden von Benutzerdetails für die Authentifizierung
 * - Zuweisen von Benutzerrollen basierend auf Benutzertyp
 * - Bereitstellen von benutzerdefinierten Benutzerdetails
 */

@Service
public class SecurityService implements UserDetailsService {

    // Repositories für Datenbankzugriff
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UnternehmenRepository unternehmenRepository;

    // Konstruktor zur Initialisierung der Repositories
    public SecurityService(UserRepository userRepository, StudentRepository studentRepository, UnternehmenRepository unternehmenRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
    }

    /**
     * Lädt Benutzerdetails basierend auf Benutzername
     * @param username Benutzername zur Authentifizierung
     * @return UserDetails des Benutzers
     * @throws UsernameNotFoundException wenn Benutzer nicht gefunden wird
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository.findByUsername(username));
    }

    /**
     * Innere Klasse für benutzerdefinierte Benutzerdetails
     * Implementiert Spring Security's UserDetails Interface
     */
    class CustomUserDetails implements UserDetails {

        private final String username;
        private final String password;
        private final List<GrantedAuthority> authorities;

        /**
         * Konstruktor zur Erstellung von Benutzerdetails
         * Setzt Benutzername, Passwort und Rollen
         * @param user Benutzer-Entität
         */
        public CustomUserDetails(User user) {
            try {
                this.username = user.getUsername();
                this.password = user.getPassword();
                this.authorities = new ArrayList<>();

                // Rollenzuweisung basierend auf Benutzertyp
                if (studentRepository.existsByUserUserID(user.getUserID())) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + Globals.Roles.STUDENT));
                } else if (unternehmenRepository.existsByUserUserID(user.getUserID())) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + Globals.Roles.UNTERNEHMEN));
                }
            } catch (Exception e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        }

        // Verschiedene Methoden zur Implementierung von UserDetails Interface

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}


