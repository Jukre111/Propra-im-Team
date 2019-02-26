package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Data
@Entity(name = "LendableItem")
public class LendableItem extends Item{

    private Integer rental;     //per Day
    private Integer deposit;

    @ElementCollection
    private final List<Period> periods = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private final List<Request> requests = new ArrayList<>();

    public LendableItem(){
    }

    public LendableItem(String name, String description, Integer rental, Integer deposit, User owner){
        super.name = name;
        super.description = description;
        this.rental = rental;
        this.deposit = deposit;
        super.owner = owner;
    }

    public LendableItem(String name, String description, Integer rental, Integer deposit, User owner, Image image){
        super.name = name;
        super.description = description;
        this.rental = rental;
        this.deposit = deposit;
        super.owner = owner;
        super.image = image;
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
