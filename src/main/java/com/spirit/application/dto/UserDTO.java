package com.spirit.application.dto;


import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;
import com.spirit.application.entitiy.Profile;

/**
 * Data Transfer Object (DTO) for representing a User.
 */
@Setter
@Getter
public class UserDTO {

    private long userID;
    private Profile profile;
    private String username;
    private String password;
    private int blacklisted;
    private String email;

    /**
     * Constructor to initialize UserDTO from a User entity.
     *
     * @param entity the User entity to convert into DTO.
     */
    public UserDTO(User entity) {
        this.userID = entity.getUserID();
        this.profile = entity.getProfile();
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
        user.setProfile(profile);
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
                ", profile=" + profile +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", blacklisted=" + blacklisted +
                ", email='" + email + '\'' +
                '}';
    }

}