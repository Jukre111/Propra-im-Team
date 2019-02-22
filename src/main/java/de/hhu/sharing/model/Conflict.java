package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Conflict {

    @Id
    @GeneratedValue
    private Long id;

    private String problem;

    @ManyToOne
    private Item item;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User borrower;

    @ManyToOne
    private BorrowingProcess process;

    public Conflict(){
    }

    public Conflict(String problem, Item item, User owner, User borrower, BorrowingProcess process){
        this.problem = problem;
        this.item = item;
        this.owner = owner;
        this.borrower = borrower;
        this.process = process;
    }

}
