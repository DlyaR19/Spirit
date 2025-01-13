package com.spirit.application.service;


import com.spirit.application.dto.BewertungDTO;
import com.spirit.application.entitiy.*;
import com.spirit.application.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user profiles.
 * Includes methods for saving, retrieving, and deleting profile information.
 */
@Service
public class ProfilService {

    private static final String PROFILE_NOT_FOUND = "Profil with ID ";
    private static final String NOT_FOUND = " not found";

    private final ProfilRepository profilRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final BewerbungRepository bewerbungRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final JobPostRepository jobPostRepository;

    public ProfilService(ProfilRepository profilRepository, UserRepository userRepository, StudentRepository studentRepository, BewerbungRepository bewerbungRepository, UnternehmenRepository unternehmenRepository, JobPostRepository jobPostRepository) {
        this.profilRepository = profilRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.bewerbungRepository = bewerbungRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.jobPostRepository = jobPostRepository;
    }

    /**
     * Saves a profile image.
     * @param profileId the profile ID
     * @param base64Image the image in Base64 format
     */
    public void saveProfileImage(Long profileId, String base64Image) {
        Optional<Profil> optionalProfile = profilRepository.findById(profileId);
        if (optionalProfile.isPresent()) {
            Profil profil = optionalProfile.get();
            profil.setAvatar(base64Image);
            profilRepository.save(profil);
        } else {
            throw new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND);
        }
    }

    /**
     * Saves the social information of a profil (e.g., website, description).
     * @param profil the profil
     * @param webseite the website URL
     * @param description the profil description
     */
    public void saveSocials(Profil profil, String webseite, String description) {
        if (webseite != null && webseite.isEmpty()){
            webseite = null;
        }
        profil.setWebseite(webseite);
        profil.setProfileDescription(description);
        profilRepository.save(profil);
    }

    /**
     * Retrieves the profile image of a profile.
     * @param profileId the profile ID
     * @return the profile image in Base64 format
     */
    public String getProfileImage(Long profileId) {
        return profilRepository.findById(profileId)
                .map(Profil::getAvatar)
                .orElseThrow(() -> new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND));
    }

    /**
     * Deletes the profile image of a profile.
     * @param profileId the profile ID
     */
    public void deleteProfileImage(Long profileId) {
        Optional<Profil> optionalProfile = profilRepository.findById(profileId);
        if (optionalProfile.isPresent()) {
            Profil profil = optionalProfile.get();
            profil.setAvatar(null);
            profilRepository.save(profil);
        } else {
            throw new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND);
        }
    }

    /**
     * Deletes a user profile and all associated data.
     * @param profileId the profile ID
     */
    @Transactional
    public void deleteProfile(Long profileId) {

        User user = userRepository.findUserByProfil_ProfilID(profileId);
        Long userId = user.getUserID();

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

        userRepository.deleteByUserID(userId);

        profilRepository.deleteByProfilID(profileId);
    }
}
