package com.spirit.application.entitiy.ids;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


// Diese Klasse repräsentiert ein BaseID-Objekt, das als Basisklasse für die ID-Entitäten dient.
@Setter
@Getter
public abstract class BaseID implements Serializable {


    private int serialNumber; //laufende Nummer

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseID baseID = (BaseID) o;
        return serialNumber == baseID.serialNumber;
    }

}