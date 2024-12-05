package com.spirit.application.dto;


import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;
import com.spirit.application.entitiy.Profile;

@Setter
@Getter
public class UserDTO {

    private long userID;
    private Profile profile;
    private String username;
    private String password;
    private int blacklisted;
    private String email;


    public UserDTO(User entity) {
        this.userID = entity.getUserID();
        this.profile = entity.getProfile();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.blacklisted = entity.getBlacklisted();
        this.email = entity.getEmail();
    }

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