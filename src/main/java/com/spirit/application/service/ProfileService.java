package com.spirit.application.service;


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
public class ProfileService {

    private static final String PROFILE_NOT_FOUND = "Profile with ID ";
    private static final String NOT_FOUND = " not found";

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final BewerbungRepository bewerbungRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final JobPostRepository jobPostRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, StudentRepository studentRepository, BewerbungRepository bewerbungRepository, UnternehmenRepository unternehmenRepository, JobPostRepository jobPostRepository) {
        this.profileRepository = profileRepository;
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
     * Saves the social information of a profile (e.g., website, description).
     * @param profile the profile
     * @param webseite the website URL
     * @param description the profile description
     */
    public void saveSocials(Profile profile, String webseite, String description) {
        if (webseite != null && webseite.isEmpty()){
            webseite = null;
        }
        profile.setWebseite(webseite);
        profile.setProfileDescription(description);
        profileRepository.save(profile);
    }

    /**
     * Retrieves the profile image of a profile.
     * @param profileId the profile ID
     * @return the profile image in Base64 format
     */
    public String getProfileImage(Long profileId) {
        return profileRepository.findById(profileId)
                .map(Profile::getAvatar)
                .orElseThrow(() -> new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND));
    }

    /**
     * Deletes the profile image of a profile.
     * @param profileId the profile ID
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

    /**
     * Deletes a user profile and all associated data.
     * @param profileId the profile ID
     */
    @Transactional
    public void deleteProfile(Long profileId) {

        User user = userRepository.findUserByProfile_ProfileID(profileId);
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

        profileRepository.deleteByProfileID(profileId);
    }

}