package com.spirit.application.dto;


import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;
import com.spirit.application.entitiy.Profil;

/**
 * Data Transfer Object (DTO) for representing a User.
 */
@Setter
@Getter
public class UserDTO {

    private long userID;
    private Profil profil;
    private String username;
    private String password;
    private int blacklisted;
    private String email;

    public UserDTO() {
    }

    /**
     * Constructor to initialize UserDTO from a User entity.
     *
     * @param entity the User entity to convert into DTO.
     */
    public UserDTO(User entity) {
        this.userID = entity.getUserID();
        this.profil = entity.getProfil();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.blacklisted = entity.getBlacklisted();
        this.email = entity.getEmail();
    }

    /**
     * Converts this DTO back to a User entity.
     *
     * @return a User entity corresponding to this DTO.
     */
    public User getUser() {
        User user = new User();
        user.setUserID(userID);
        user.setProfil(profil);
        user.setUsername(username);
        user.setPassword(password);
        user.setBlacklisted(blacklisted);
        user.setEmail(email);
        return user;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userID=" + userID +
                ", profil=" + profil +
                ", username='" + username + '\'' +
                ", blacklisted=" + blacklisted +
                ", email='" + email + '\'' +
                '}';
    }

}