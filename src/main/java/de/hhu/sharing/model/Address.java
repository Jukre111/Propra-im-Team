package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {

    private String street;
    private String city;
    private int postcode;

    public Address(){
    }

    public Address(String street, String city, int postcode){
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }

}
