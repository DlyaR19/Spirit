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
 * Service class for security functions.
 * Implements UserDetailsService for authentication and authorization.
 */
@Service
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UnternehmenRepository unternehmenRepository;

    public SecurityService(UserRepository userRepository, StudentRepository studentRepository, UnternehmenRepository unternehmenRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
    }

    /**
     * Loads user details based on the username.
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository.findByUsernameIgnoreCase(username));
    }

    /**
     * Custom implementation of UserDetails.
     * Includes user data such as username, password, and roles.
     */
    class CustomUserDetails implements UserDetails {

        private final String username;
        private final String password;
        private final List<GrantedAuthority> authorities;

        public CustomUserDetails(User user) {
            // Initializes username, password, and roles based on the user type
            try {
                this.username = user.getUsername();
                this.password = user.getPassword();
                this.authorities = new ArrayList<>();

                if (studentRepository.existsByUserUserID(user.getUserID())) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + Globals.Roles.STUDENT));
                } else if (unternehmenRepository.existsByUserUserID(user.getUserID())) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + Globals.Roles.UNTERNEHMEN));
                }
            } catch (Exception e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        }

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


