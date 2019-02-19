package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Request {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private RentPeriod period;

    @ManyToOne
    private User requester;


    public Request(){
    }

    public Request(LocalDate startdate, LocalDate enddate, User requester){
        this.period = new RentPeriod(startdate, enddate);
        this.requester = requester;
    }

}
