package com.spirit.application;

import com.spirit.application.service.SecurityService;
import com.spirit.application.util.Globals;
import com.spirit.application.views.login.LoginView;
import com.vaadin.flow.component.charts.model.Global;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    final SecurityService securityService;
    final PasswordEncoder passwordEncoder;

    public SecurityConfig(SecurityService securityService) {
        this.securityService = securityService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(securityService, passwordEncoder);
    }

    private record CustomAuthenticationProvider(SecurityService securityService, PasswordEncoder passwordEncoder) implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            try {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                UserDetails userDetails = securityService.loadUserByUsername(username);

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

        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}
