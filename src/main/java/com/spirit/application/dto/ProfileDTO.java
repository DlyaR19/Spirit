package com.spirit.application.dto;


import com.spirit.application.entitiy.Profile;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing a User Profile.
 */
@Setter
@Getter
public class ProfileDTO {

    private long profileID;
    private String avatarUrl;
    private String profileDescription;
    private String webseite;

    /**
     * Constructor to initialize ProfileDTO from a Profile entity.
     *
     * @param entity the Profile entity to convert into DTO.
     */
    public ProfileDTO(Profile entity) {
        this.profileID = entity.getProfileID();
        this.avatarUrl = entity.getAvatar();
        this.profileDescription = entity.getProfileDescription();
        this.webseite = entity.getWebseite();
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "profileID=" + profileID +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", webseite='" + webseite + '\'' +
                '}';
    }
}
