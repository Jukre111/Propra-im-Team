package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class BorrowRequest {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate startdate;
    private LocalDate enddate;

    @ManyToOne
    private User requester;

    public BorrowRequest(){
    }

    public BorrowRequest(LocalDate startdate, LocalDate enddate, User requester){
        this.startdate = startdate;
        this.enddate = enddate;
        this.requester = requester;
    }

}
