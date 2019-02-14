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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> borrowedItems = new ArrayList<>();

    public void addToBorrowedItem(Item item) {
        borrowedItems.add(item);
    }

    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private List<ItemNotFound> lendItems = new ArrayList<>();

}
