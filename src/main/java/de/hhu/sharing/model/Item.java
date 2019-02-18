package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private boolean available = true;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Request> requests = new ArrayList<>();

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

}
