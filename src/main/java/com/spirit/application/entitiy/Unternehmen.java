package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a company in the system.
 */
@Getter
@Setter
@Entity
@Table(name = "unternehmen", schema = "public")
public class Unternehmen implements Serializable {

    /**
     * The unique ID of the company.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unternehmenid", length = 64, nullable = false)
    private long unternehmenID;

    /**
     * The user account associated with the company.
     */
    @OneToOne
    @JoinColumn(name = "userid", nullable = false, unique = true)
    private User user;

    /**
     * The name of the company.
     */
    @Basic
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    /**
     * The address of the company.
     */
    @Basic
    @Column(name = "adresse", length = 128)
    private String address;

    /**
     * The city where the company is located.
     */
    @Basic
    @Column(name = "stadt", length = 128)
    private String city;

    /**
     * The postal code of the company's location.
     */
    @Basic
    @Column(name = "postleitzahl", length = 8)
    private String zipCode;

    /**
     * The country where the company is located.
     */
    @Basic
    @Column(name = "land", length = 128)
    private String country;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unternehmen unternehmen = (Unternehmen) o;
        return unternehmenID == unternehmen.unternehmenID &&
                Objects.equals(user, unternehmen.user) &&
                Objects.equals(name, unternehmen.name) &&
                Objects.equals(address, unternehmen.address) &&
                Objects.equals(city, unternehmen.city) &&
                Objects.equals(zipCode, unternehmen.zipCode) &&
                Objects.equals(country, unternehmen.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unternehmenID, user, name, address, city, zipCode, country);
    }
}
