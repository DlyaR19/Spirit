package com.spirit.application;

import com.spirit.application.service.SecurityService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Konfiguriert die Sicherheitseinstellungen der Anwendung
 * Implementiert Spring Security mit Vaadin-Integration
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    final SecurityService securityService;
    final PasswordEncoder passwordEncoder;

    /**
     * Konstruktor initialisiert Sicherheitsdienste
     */
    public SecurityConfig(SecurityService securityService) {
        this.securityService = securityService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Konfiguriert die HTTP-Sicherheitseinstellungen
     * @param http HttpSecurity-Objekt für Konfiguration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }
    
//    @Configuration
//    public static class VaadinResourceConfig extends VaadinWebSecurity {
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().requestMatchers(
//                    new AntPathRequestMatcher("/images/**")
//            );
//        }
//    }

    /**
     * Hash-Encoder für Passwörter
     * @return PasswordEncoder-Objekt
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom Authentication Provider für die Benutzerauthentifizierung
     * @return AuthenticationProvider-Objekt
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(securityService, passwordEncoder);
    }

    /**
     * Custom Authentication Provider für die Benutzerauthentifizierung
     */
    private record CustomAuthenticationProvider(SecurityService securityService, PasswordEncoder passwordEncoder) implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            // Authentifizierungslogik
            try {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                UserDetails userDetails = securityService.loadUserByUsername(username);

                // Überprüfung der Anmeldedaten
                if (userDetails == null) {
                    throw new BadCredentialsException("User not found");
                }
                if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                    throw new BadCredentialsException("Invalid password");
                }
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            } catch (Exception e) {
                throw new BadCredentialsException("Authentication failed: " + e.getMessage());
            }
        }

        /**
         * Überprüft, ob der AuthenticationProvider das gegebene Authentifizierungsobjekt unterstützt
         * @param authentication Authentifizierungsobjekt
         * @return true, wenn das Objekt unterstützt wird
         */
        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}
