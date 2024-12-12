package com.spirit.application.service;


import com.spirit.application.entitiy.*;
import com.spirit.application.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ProfileService - Verwaltet Profilbezogene Operationen
 * Verantwortlichkeiten:
 * - Speichern und Verwalten von Profilbildern
 * - Aktualisieren von Profil-Metadaten
 * - Behandeln von Profilbild-Operationen
 */

@Service
public class ProfileService {

    // Konstanten für Fehlermeldungen
    private static final String PROFILE_NOT_FOUND = "Profile with ID ";
    private static final String NOT_FOUND = " not found";

    // Repository für Profilzugriff
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final BewerbungRepository bewerbungRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final JobPostRepository jobPostRepository;

    // Konstruktor zur Initialisierung des ProfileRepository
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, StudentRepository studentRepository, BewerbungRepository bewerbungRepository, UnternehmenRepository unternehmenRepository, JobPostRepository jobPostRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.bewerbungRepository = bewerbungRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.jobPostRepository = jobPostRepository;
    }

    /**
     * Speichert ein Profilbild für ein gegebenes Profil
     * @param profileId ID des Profils
     * @param base64Image Base64-kodiertes Profilbild
     * @throws IllegalArgumentException wenn Profil nicht gefunden wird
     */
    public void saveProfileImage(Long profileId, String base64Image) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            profile.setAvatar(base64Image);
            profileRepository.save(profile);
        } else {
            throw new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND);
        }
    }

    /**
     * Speichert Social-Media-Informationen für ein Profil
     * @param profile Das zu aktualisierende Profil
     * @param webseite Webseite
     * @param description Profil-Beschreibung
     */
    public void saveSocials(Profile profile, String webseite, String description) {
        // Leere Webseite-URLs werden auf null gesetzt
        if (webseite != null && webseite.isEmpty()){
            webseite = null;
        }
        profile.setWebseite(webseite);
        profile.setProfileDescription(description);
        profileRepository.save(profile);
    }

    /**
     * Holt das Profilbild für eine gegebene Profil-ID
     * @param profileId ID des Profils
     * @return Base64-kodiertes Profilbild
     * @throws IllegalArgumentException wenn Profil nicht gefunden wird
     */
    public String getProfileImage(Long profileId) {
        return profileRepository.findById(profileId)
                .map(Profile::getAvatar)
                .orElseThrow(() -> new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND));
    }

    /**
     * Löscht das Profilbild für eine gegebene Profil-ID
     * @param profileId ID des Profils
     * @throws IllegalArgumentException wenn Profil nicht gefunden wird
     */
    public void deleteProfileImage(Long profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            profile.setAvatar(null);
            profileRepository.save(profile);
        } else {
            throw new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND);
        }
    }

    // profil löschen
    @Transactional
    public void deleteProfile(Long profileId) {

        // Zuerst holen wir das User Objekt, das auf das Profil verweist
        User user = userRepository.findUserByProfile_ProfileID(profileId);
        Long userId = user.getUserID();

        // Zuerst holen wir den Studenten/Unternehmen, der auf den Benutzer verweist
        Student student = studentRepository.findStudentByUserUserID(userId);
        Unternehmen unternehmen = unternehmenRepository.findUnternehmenByUserUserID(userId);


        if (student != null) {
            bewerbungRepository.deleteBewerbungByStudent_StudentID(student.getStudentID());
            studentRepository.deleteByUserUserID(userId);
        } else if (unternehmen != null) {
            jobPostRepository.deleteJobPostByUnternehmen_UnternehmenID(unternehmen.getUnternehmenID());
            unternehmenRepository.deleteByUserUserID(userId);
        } else {
            throw new IllegalArgumentException(NOT_FOUND);
        }

        // Dann löschen wir den Benutzer
        userRepository.deleteByUserID(userId);

        // Schließlich löschen wir das Profil, falls es auf den Benutzer verweist
        profileRepository.deleteByProfileID(profileId);
    }

}