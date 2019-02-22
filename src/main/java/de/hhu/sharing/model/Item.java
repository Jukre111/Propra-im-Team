package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
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

    @ManyToOne
    private User lender;

    @ElementCollection
    private final List<Period> periods = new ArrayList<>();

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

    @Transactional
    public void addToPeriods(Period period){
        this.periods.add(period);
    }

    @Transactional
    public void removeFromPeriods(Period period){
        this.periods.remove(period);
    }

    @Transactional
    public boolean noPeriodsAndRequests() {
        return periods.isEmpty() && requests.isEmpty();
    }

    @Transactional
    public boolean isAvailableAt(Period period) {
        for(Period per : periods){
            if(per.overlapsWith(period)){
                return false;
            }
        }
        return true;

    }
}
