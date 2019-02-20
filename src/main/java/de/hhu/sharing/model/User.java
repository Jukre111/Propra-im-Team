package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="\"User\"")
public class User {

    @Id
    private String username;
    
    private String password;
    private String role;

    private String lastname;
    private String forename;
    private String email;
    private LocalDate birthdate;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Item> borrowedItems = new ArrayList<>();

    public User(){
    }

    public User(String username, String password, String role,
                String lastname, String forename, String email,
                LocalDate birthdate, Address address){
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastname = lastname;
        this.forename = forename;
        this.email = email;
        this.birthdate = birthdate;
        this.address = address;
    }

    public void addToBorrowedItems(Item item) {
        //borrowedItems.add(item);
    }

}
