package com.spirit.application.service;


import com.spirit.application.entitiy.Profile;
import com.spirit.application.repository.ProfileRepository;
import org.springframework.stereotype.Service;

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

    // Konstruktor zur Initialisierung des ProfileRepository
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
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
}