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

    private LocalDate startdate;
    private LocalDate enddate;

    @ManyToOne
    private User requester;

    public Request(){
    }

    public Request(LocalDate startdate, LocalDate enddate, User requester){
        this.startdate = startdate;
        this.enddate = enddate;
        this.requester = requester;
    }

}
