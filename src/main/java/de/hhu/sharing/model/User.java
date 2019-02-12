package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="\"User\"")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String password;
    private String mail;
    private Date birthdate;

    private String role;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> borrowedItems = new ArrayList<>();

    public void addToBorrowedItem(Item item) {
        borrowedItems.add(item);
    }

    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private List<Item> lendItems = new ArrayList<>();

}
