package com.spirit.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BewertungDTO {
    private long bewertungID;
    private long profil;
    private long user;
    private Double averageRating;
    private Integer totalRatings;
    private long ratedUser;

    public BewertungDTO() {
    }

    public BewertungDTO(long bewertungID, long profil, long user, Double averageRating, Integer totalRatings, long ratedUser) {
        this.bewertungID = bewertungID;
        this.profil = profil;
        this.user = user;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
        this.ratedUser = ratedUser;
    }

    @Override
    public String toString() {
        return "BewertungDTO{" +
                "bewertungID=" + bewertungID +
                ", profil=" + profil +
                ", user=" + user +
                ", averageRating=" + averageRating +
                ", totalRatings=" + totalRatings +
                ", ratedUser=" + ratedUser +
                '}';
    }
}
