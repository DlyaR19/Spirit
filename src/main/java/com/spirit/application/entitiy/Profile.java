package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


/**
 * Represents a user profile in the system.
 */
@Setter
@Getter
@Entity
@Table(name = "profil", schema = "public")
public class Profile implements Serializable {

    /**
     * The unique ID of the profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profilid", nullable = false)
    private long profileID;

    /**
     * The avatar image for the profile, stored as text (e.g., a URL or base64 encoded string).
     */
    @Basic
    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    /**
     * A description for the profile.
     */
    @Basic
    @Column(name = "profilbeschreibung", length = 6400)
    private String profileDescription;

    /**
     * The personal website associated with the profile.
     */
    @Basic
    @Column(name = "webseite", length = 2048, unique = true)
    private String webseite;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return profileID == profile.profileID &&
                Objects.equals(avatar, profile.avatar) &&
                Objects.equals(profileDescription, profile.profileDescription) &&
                Objects.equals(webseite, profile.webseite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileID, avatar, profileDescription, webseite);
    }
}
