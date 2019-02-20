package de.hhu.sharing.model;

import lombok.Data;
import org.hibernate.Hibernate;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.*;

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
    private int reservationId;

    @ManyToOne
    private User lender;

    @OneToMany
    private final List<RentPeriod> periods = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private final List<Request> requests = new ArrayList<>();

    public Item(){
    }

    public Item(String name, String description, int rental, int deposit, User lender){
        this.name = name;
        this.description = description;
        this.rental = rental;
        this.deposit = deposit;
        this.lender = lender;
    }

    public void addToRequests(Request request) {
        requests.add(request);
    }

    public void removeFromRequests(Request request) {
        requests.remove(request);
    }

    public void addToPeriods(RentPeriod period){
        this.periods.add(period);
    }

    public void removeOverlappingRequests(Request req) {
        requests.removeIf(request -> request.overlapesWith(req));
    }
}
