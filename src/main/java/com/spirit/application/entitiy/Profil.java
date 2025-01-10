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
public class Profil implements Serializable {

    /**
     * The unique ID of the profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profilid", nullable = false)
    private long profilID;

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

    @Basic
    @Column(name = "avg_rating", nullable = false)
    private Double avgRating = 0.0;

    @Basic
    @Column(name = "totalrating", nullable = false)
    private Integer totalRating = 0;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profil profil = (Profil) o;
        return profilID == profil.profilID &&
                Objects.equals(avatar, profil.avatar) &&
                Objects.equals(profileDescription, profil.profileDescription) &&
                Objects.equals(webseite, profil.webseite) &&
                Objects.equals(avgRating, profil.avgRating) &&
                Objects.equals(totalRating, profil.totalRating);
     }

    @Override
    public int hashCode() {
        return Objects.hash(profilID, avatar, profileDescription, webseite, avgRating, totalRating);
    }
}
