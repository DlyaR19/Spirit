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
 * Security configuration for the application, extending {@link VaadinWebSecurity} to configure
 * custom security features for Vaadin-based applications.
 * <p>It sets up a custom login view, password encoding, and authentication provider.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @EnableWebSecurity}: Enables web security configuration for the application.</li>
 *   <li>{@code @Configuration}: Marks this class as a configuration class for Spring Security.</li>
 * </ul>
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    final SecurityService securityService;
    final PasswordEncoder passwordEncoder;

    /**
     * Constructor initializes the security services required for authentication.
     * @param securityService the {@link SecurityService} used for loading user details and handling authentication.
     */
    public SecurityConfig(SecurityService securityService) {
        this.securityService = securityService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security settings.
     * <p>Sets the login view to {@link LoginView}.</p>
     * @param http the {@link HttpSecurity} object to configure the HTTP security.
     * @throws Exception if an error occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    /**
     * Bean definition for the {@link PasswordEncoder} used for hashing passwords.
     * @return a {@link PasswordEncoder} instance, specifically a {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean definition for the custom authentication provider used in the application.
     * <p>This provider handles the authentication of users by verifying the username and password.</p>
     * @return an {@link AuthenticationProvider} instance configured with the security service and password encoder.
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(securityService, passwordEncoder);
    }

    /**
     * Custom authentication provider for user authentication.
     * <p>This implementation verifies user credentials against the database using {@link SecurityService}.</p>
     */
    private record CustomAuthenticationProvider(SecurityService securityService, PasswordEncoder passwordEncoder) implements AuthenticationProvider {

        /**
         * Authenticates the provided {@link Authentication} object.
         * <p>It verifies that the username exists and that the password matches the stored password.</p>
         * @param authentication the {@link Authentication} object containing user credentials.
         * @return an authenticated {@link Authentication} object if credentials are valid.
         * @throws AuthenticationException if authentication fails.
         */
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
         * Determines whether the given {@link Authentication} type is supported.
         * @param authentication the {@link Authentication} object to check.
         * @return true if the authentication object is of type {@link UsernamePasswordAuthenticationToken}, false otherwise.
         */
        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}
