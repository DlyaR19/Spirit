package com.spirit.application.dto;


import com.spirit.application.entitiy.Profil;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing a User Profil.
 */
@Setter
@Getter
public class ProfilDTO {

    private long profileID;
    private String avatarUrl;
    private String profileDescription;
    private String webseite;
    private Double avgRating;
    private Integer totalRating;

    /**
     * Constructor to initialize ProfilDTO from a Profil entity.
     *
     * @param entity the Profil entity to convert into DTO.
     */
    public ProfilDTO(Profil entity) {
        this.profileID = entity.getProfilID();
        this.avatarUrl = entity.getAvatar();
        this.profileDescription = entity.getProfileDescription();
        this.webseite = entity.getWebseite();
        this.avgRating = entity.getAvgRating();
        this.totalRating = entity.getTotalRating();
    }

    @Override
    public String toString() {
        return "ProfilDTO{" +
                "profilID=" + profileID +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", webseite='" + webseite + '\'' +
                ", avgRating=" + avgRating +
                ", totalRating=" + totalRating +
                '}';
    }
}
