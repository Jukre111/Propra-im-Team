package de.hhu.sharing.model;

import lombok.Data;
import org.hibernate.annotations.IndexColumn;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="\"User\"")
public class User {

    @Id
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String role;
    @NotNull
    private String lastname;
    @NotNull
    private String forename;
    @NotNull
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @OneToOne
    @Valid
    private Image image;
    
    @Embedded
    @NotNull
    @Valid
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="borrowed")
    @IndexColumn(base = 1, name = "bor")
    private final List<@Valid BorrowingProcess> borrowed = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="lend")
    @IndexColumn(base = 1, name = "len")
    private final List<@Valid BorrowingProcess> lend = new ArrayList<>();

    @ElementCollection
    private List<String> purchaseinformations = new ArrayList<>();

    @ElementCollection
    private List<String> saleinformations = new ArrayList<>();

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
    
    public User(String username, String password, String role,
            String lastname, String forename, String email,
            LocalDate birthdate, Address address, Image image){
    	this.username = username;
    	this.password = password;
    	this.role = role;
    	this.lastname = lastname;
    	this.forename = forename;
    	this.email = email;
    	this.birthdate = birthdate;
    	this.address = address;
    	this.image = image;
    }

    public void addToBorrowed(BorrowingProcess process) {
        this.borrowed.add(process);
    }

    public void removeFromBorrowed(BorrowingProcess process) {
        this.borrowed.remove(process);
    }

    public void addToLend(BorrowingProcess process) {
        this.lend.add(process);
    }

    public void removeFromLend(BorrowingProcess process) {
        this.lend.remove(process);
    }

    @Transactional
    public void addToPurchaseinformations(String information) {
        this.purchaseinformations.add(information);
    }

    @Transactional
    public void addToSaleinformations(String information) {
        this.saleinformations.add(information);
    }
}
