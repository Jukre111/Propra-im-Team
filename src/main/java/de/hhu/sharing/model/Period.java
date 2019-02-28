package de.hhu.sharing.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Embeddable
public class Period {


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    public Period(){
    }

    public Period(LocalDate startdate, LocalDate enddate){
        this.startdate = startdate;
        this.enddate = enddate;
    }


    public boolean overlapsWith(Period period) {
        return startdate.isEqual(period.getStartdate())
                || startdate.isEqual(period.getEnddate())
                || enddate.isEqual(period.getStartdate())
                || enddate.isEqual(period.getEnddate())
                || startdate.isBefore(period.getStartdate()) && enddate.isAfter(period.getStartdate())
                || startdate.isBefore(period.getEnddate()) && enddate.isAfter(period.getEnddate())
                || startdate.isAfter(period.getStartdate()) && enddate.isBefore(period.getEnddate());
    }

    public boolean isOutdated() {
        return startdate.isBefore(LocalDate.now());
    }
}
