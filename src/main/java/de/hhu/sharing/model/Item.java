package de.hhu.sharing.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<BorrowRequest> requests = new ArrayList<>();

    public Item(){
    }

    public Item(String name, String description, int rental, int deposit, User lender){
        this.name = name;
        this.description = description;
        this.rental = rental;
        this.deposit = deposit;
        this.lender = lender;
    }

}
