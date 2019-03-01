package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
public class Address {

    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    @Min(0)
    private int postcode;

    public Address(){
    }

    public Address(String street, String city, int postcode){
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }

}
