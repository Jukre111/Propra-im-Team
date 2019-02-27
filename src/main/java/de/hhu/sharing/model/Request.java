package de.hhu.sharing.model;

import lombok.Data;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class Request {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @NotNull
    @Valid
    private Period period;

    @ManyToOne
    @NotNull
    @Valid
    private User requester;


    public Request(){
    }

    public Request(Period period, User requester){
        this.period = period;
        this.requester = requester;
    }
}
