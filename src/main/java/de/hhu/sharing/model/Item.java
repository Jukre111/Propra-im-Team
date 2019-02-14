package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private int rental;     //per Day
    private int deposit;
    private LocalDate startdate;
    private LocalDate enddate;

    @ManyToOne(fetch = FetchType.EAGER)
    private User lender;

    //@ManyToOne(fetch = FetchType.EAGER)
    //private User borrower;


}
