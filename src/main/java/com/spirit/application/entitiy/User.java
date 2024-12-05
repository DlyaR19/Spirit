package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "user", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false)
    private long userID;
    @OneToOne
    @JoinColumn(name = "profilid", nullable = false, unique = true)
    private Profile profile;
    @Column(name = "username", nullable = false, length = 32, unique = true)
    private String username;
    @Column(name = "passwort", nullable = false, length = 64, unique = true)
    private String password;
    @Column(name = "blacklisted", nullable = false, length = 1)
    private int blacklisted;
    @Column(name = "email", nullable = false, length = 320, unique = true)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID == user.userID &&
                blacklisted == user.blacklisted &&
                Objects.equals(profile, user.profile) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    public int hashCode() {
        return Objects.hash(userID, profile, username, password, blacklisted);
    }



}
