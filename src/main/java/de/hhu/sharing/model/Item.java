package de.hhu.sharing.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Column(columnDefinition = "TEXT")
    private String description;
    private int rental;     //per Day
    private int deposit;
    private LocalDate startdate;
    private LocalDate enddate;

    @ManyToOne(fetch = FetchType.EAGER)
    private User lender;

    public Item(){
    }

    public Item(String name, String description, int rental, int deposit, LocalDate startdate, LocalDate enddate, User lender){
        this.name = name;
        this.description = description;
        this.rental = rental;
        this.deposit = deposit;
        this.startdate = startdate;
        this.enddate = enddate;
        this.lender = lender;
    }

}
