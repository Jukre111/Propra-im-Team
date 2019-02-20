package de.hhu.sharing.model;

import lombok.Data;
import org.apache.tomcat.jni.Local;

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

    public boolean overlapesWith(Request request) {
        return startdate.isEqual(request.getStartdate())
                || startdate.isEqual(request.getEnddate())
                || enddate.isEqual(request.getStartdate())
                || enddate.isEqual(request.getEnddate())
                || startdate.isBefore(request.getStartdate()) && enddate.isAfter(request.getStartdate())
                || startdate.isBefore(request.getEnddate()) && enddate.isAfter(request.getEnddate())
                || startdate.isAfter(request.getStartdate()) && enddate.isBefore(request.getEnddate());
    }

}
