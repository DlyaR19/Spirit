package com.spirit.application.service;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.*;
import com.spirit.application.repository.*;
import com.spirit.application.util.Globals;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final BewertungRepository bewertungRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final ProfilRepository profilRepository;

    public UserService(BewertungRepository bewertungRepository, UserRepository userRepository,
                       StudentRepository studentRepository,
                       UnternehmenRepository unternehmenRepository, ProfilRepository profilRepository) {
        this.bewertungRepository = bewertungRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.profilRepository = profilRepository;
    }

    /**
     * Gibt alle Benutzer als DTOs zurück
     * @return Liste von UserDTOs
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Prüft ob ein Benutzer ein Student ist
     * @param userId Benutzer-ID
     * @return true wenn Student
     */
    public boolean isStudent(Long userId) {
        Student student = studentRepository.findStudentByUserUserID(userId);
        return student != null;
    }

    /**
     * Prüft ob ein Benutzer ein Unternehmen ist
     * @param userId Benutzer-ID
     * @return true wenn Unternehmen
     */
    public boolean isUnternehmen(Long userId) {
        Unternehmen unternehmen = unternehmenRepository.findUnternehmenByUserUserID(userId);
        return unternehmen != null;
    }

    /**
     * Gibt den aktuell eingeloggten Benutzer zurück
     * @return UserDTO des aktuellen Benutzers
     */
    public UserDTO getCurrentUser() {
        return (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
    }

    /**
     * Sucht Benutzer nach verschiedenen Kriterien
     * @param searchTerm Suchbegriff (Username oder Email)
     * @param userType Benutzertyp (STUDENT, UNTERNEHMEN, oder null für alle)
     * @return gefilterte Liste von UserDTOs
     */
    public List<UserDTO> searchUsers(String searchTerm, String userType) {
        List<UserDTO> allUsers = getAllUsers();

        return allUsers.stream()
                .filter(user -> {
                    // Filter nach Suchbegriff
                    boolean matchesSearch = searchTerm == null || searchTerm.isEmpty() ||
                            user.getUsername().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(searchTerm.toLowerCase());

                    // Filter nach Benutzertyp (Student oder Unternehmen)
                    boolean matchesType = userType == null || userType.isEmpty() || "Alle".equals(userType) ||
                            (userType.equals("Studenten") && isStudent(user.getUserID())) ||
                            (userType.equals("Unternehmen") && isUnternehmen(user.getUserID()));

                    return matchesSearch && matchesType;
                })
                .collect(Collectors.toList());
    }

    /**
     * Findet einen Benutzer anhand seiner ID
     * @param userId Benutzer-ID
     * @return UserDTO oder null
     */
    public UserDTO findById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDTO::new)
                .orElse(null);
    }

    /**
     * Aktualisiert die Bewertung eines Benutzers
     * @param userId Benutzer-ID
     * @param rating Die abgegebene Bewertung (1 bis 5)
     */
    public void rateUser(Long userId, int rating) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDTO toDTO = new UserDTO(user);
        // Überprüfen, ob der Benutzer bereits bewertet wurde
        UserDTO currentUser = getCurrentUser();
        if (hasRated(currentUser, toDTO)) {
            throw new IllegalArgumentException("Sie haben diesen Benutzer bereits bewertet.");
        }

        // Holen des Profils des Benutzers
        Profil profil = user.getProfil();
        if (profil == null) {
            throw new IllegalArgumentException("User profile not found");
        }

        // Aktualisiere die Bewertung des Profils
        Double currentAvgRating = profil.getAvgRating();
        Integer totalRatings = profil.getTotalRating() != null ? profil.getTotalRating() : 0;

        // Berechne die neue Durchschnittsbewertung
        double newAvgRating = ((currentAvgRating != null ? currentAvgRating * totalRatings : 0) + rating) / (totalRatings + 1);

        // Setze die neuen Werte
        profil.setAvgRating(newAvgRating);
        profil.setTotalRating(totalRatings + 1);

        // Speichere die Änderungen
        profilRepository.save(profil);

        // Speichere die Bewertung selbst
        Bewertung newBewertung = new Bewertung();
        newBewertung.setUser(currentUser.getUser()); // Der aktuell eingeloggte Benutzer
        newBewertung.setRatedUser(user); // Der bewertete Benutzer
        newBewertung.setStars(rating);
        newBewertung.setProfil(profil);
        bewertungRepository.save(newBewertung); // Speichere die Bewertung
    }

    public boolean hasRated(UserDTO byUser, UserDTO ratedUser) {
        User user1 = byUser.getUser();
        User user2 = ratedUser.getUser();
        Bewertung existingRating = bewertungRepository.findByUserAndRatedUser(user1, user2);
        return existingRating != null;
    }

}