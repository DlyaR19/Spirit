package com.spirit.application.service;


import com.spirit.application.entitiy.Profile;
import com.spirit.application.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private static final String PROFILE_NOT_FOUND = "Profile with ID ";
    private static final String NOT_FOUND = " not found";
    private final ProfileRepository profileRepository;


    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

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

    public void saveSocials(Profile profile, String linkedIn, String description) {
        if (linkedIn != null && linkedIn.isEmpty()){
            linkedIn = null; // set to null if empty
        }
        profile.setLinkedinUsername(linkedIn);
        profile.setProfileDescription(description);
        profileRepository.save(profile);
    }

    public String getProfileImage(Long profileId) {
        return profileRepository.findById(profileId)
                .map(Profile::getAvatar)
                .orElseThrow(() -> new IllegalArgumentException(PROFILE_NOT_FOUND + profileId + NOT_FOUND));
    }

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