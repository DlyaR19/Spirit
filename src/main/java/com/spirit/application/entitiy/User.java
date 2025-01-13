package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Represents a user in the system.
 */
@Setter
@Getter
@Entity
@Table(name = "user", schema = "public")
public class User {

    /**
     * The unique ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false)
    private long userID;

    /**
     * The profil associated with the user.
     */
    @OneToOne
    @JoinColumn(name = "profilid", nullable = false, unique = true)
    private Profil profil;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false, length = 32, unique = true)
    private String username;

    /**
     * The hashed password of the user.
     */
    @ToString.Exclude
    @Column(name = "passwort", nullable = false, length = 64, unique = true)
    private String password;

    /**
     * A flag indicating whether the user is blacklisted.
     */
    @Column(name = "blacklisted", nullable = false, length = 1)
    private int blacklisted;

    /**
     * The email address of the user.
     */
    @Column(name = "email", nullable = false, length = 320, unique = true)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID == user.userID &&
                blacklisted == user.blacklisted &&
                Objects.equals(profil, user.profil) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    public int hashCode() {
        return Objects.hash(userID, profil, username, password, blacklisted);
    }

    /**
     * Sets the username to lowercase.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username != null ? username.toLowerCase() : null;
    }
}
