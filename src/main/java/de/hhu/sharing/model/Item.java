package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
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
    private Date startdate;
    private Date enddate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User lender;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User borrower;


}
