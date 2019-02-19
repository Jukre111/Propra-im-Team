package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Embeddable
public class RentPeriod {

    private LocalDate startdate;
    private LocalDate enddate;

    public RentPeriod(){
    }

    public RentPeriod(LocalDate startdate, LocalDate enddate){
        this.startdate = startdate;
        this.enddate = enddate;
    }
}
