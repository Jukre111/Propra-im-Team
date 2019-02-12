package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;
    private String mail;
    private Date birthdate;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> borrowedItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> lendItems = new ArrayList<>();

}
