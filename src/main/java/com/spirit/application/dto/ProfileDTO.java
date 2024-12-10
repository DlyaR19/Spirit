package com.spirit.application.dto;


import com.spirit.application.entitiy.Profile;
import lombok.Getter;
import lombok.Setter;

/**
 * Diese Klasse repräsentiert ein ProfileDTO-Objekt, das als Datenübertragungsobjekt für die Profile-Entität dient.
 */

@Setter
@Getter
public class ProfileDTO {

    private long profileID;
    private String avatarUrl;
    private String profileDescription;
    private String webseite;

    public ProfileDTO(Profile entity) {
        this.profileID = entity.getProfileID();
        this.avatarUrl = entity.getAvatar();
        this.profileDescription = entity.getProfileDescription();
        this.webseite = entity.getWebseite();
    }

    public String toString() {
        return "ProfileDTO{" +
                "profileID=" + profileID +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", webseite='" + webseite + '\'' +
                '}';
    }
}
