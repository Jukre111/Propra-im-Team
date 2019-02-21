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

    @Embedded
    private Period period;

    @ManyToOne
    private User requester;


    public Request(){
    }

    public Request(Period period, User requester){
        this.period = period;
        this.requester = requester;
    }



}
