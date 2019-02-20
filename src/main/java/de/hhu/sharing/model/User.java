package de.hhu.sharing.model;

import lombok.Data;
import org.hibernate.annotations.IndexColumn;

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
    @JoinTable(name="borrowed")
    @IndexColumn(base = 1, name = "bor")
    private final List<BorrowingProcess> borrowed = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="lend")
    @IndexColumn(base = 1, name = "len")
    private final List<BorrowingProcess> lend = new ArrayList<>();

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

    public void addToBorrowed(BorrowingProcess process) {
        this.borrowed.add(process);
    }

    public void addToLend(BorrowingProcess process) {
        this.lend.add(process);
    }
}
