package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class RentPeriod {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate startdate;
    private LocalDate enddate;

    @ManyToOne
    private User borrower;

    public RentPeriod(){
    }

    public RentPeriod(LocalDate startdate, LocalDate enddate, User borrower){
        this.startdate = startdate;
        this.enddate = enddate;
        this.borrower = borrower;
    }
}
