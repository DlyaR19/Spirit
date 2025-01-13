package com.spirit.application.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "bewertung", schema = "public")
@Getter
@Setter
public class Bewertung implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewertungid", nullable = false)
    private long bewertungID;

    @ManyToOne
    @JoinColumn(name = "profilid", nullable = false)
    private Profil profil;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rateduser", nullable = false)
    private User ratedUser;

    @Basic
    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bewertung bewertung = (Bewertung) o;
        return bewertungID == bewertung.bewertungID &&
                stars.equals(bewertung.stars) &&
                profil.equals(bewertung.profil) &&
                user.equals(bewertung.user) &&
                ratedUser.equals(bewertung.ratedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bewertungID, profil, user, stars, ratedUser);
    }

}
