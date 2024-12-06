package com.spirit.application.dto;


import com.spirit.application.entitiy.Unternehmen;
import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;

// Diese Klasse repräsentiert ein UnternehmenDTO-Objekt, das als Datenübertragungsobjekt für die Unternehmen-Entität dient.
@Setter
@Getter
public class UnternehmenDTO extends UserDTO {

    private long unternehmenID;
    private User user;
    private String name;
    private String address;
    private String city;
    private String zipCode;
    private String country;

    public UnternehmenDTO(Unternehmen entity) {
        super(entity.getUser());
        this.user = entity.getUser();
        this.unternehmenID = entity.getUnternehmenID();
        this.name = entity.getName();
        this.address = entity.getAddress();
        this.city = entity.getCity();
        this.zipCode = entity.getZipCode();
        this.country = entity.getCountry();
    }

    public Unternehmen getUnternehmen() {
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(unternehmenID);
        unternehmen.setName(name);
        unternehmen.setUser(user);
        unternehmen.setAddress(address);
        unternehmen.setCity(city);
        unternehmen.setZipCode(zipCode);
        unternehmen.setCountry(country);
        return unternehmen;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "BusinessDTO{" +
                "unternehmenID=" + unternehmenID +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
