package de.hhu.sharing.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString(exclude = {"lender", "borrower", "process"})
public class Conflict {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Valid
    private User lender;

    @ManyToOne
    @Valid
    private User borrower;

    @OneToOne
    @Valid
    private BorrowingProcess process;

    @ElementCollection
    private List<@Valid Message> messages = new ArrayList<>();

    public Conflict(){
    }

    public Conflict(User lender, User borrower, BorrowingProcess process, Message message){
        this.lender = lender;
        this.borrower = borrower;
        this.process = process;
        this.addToMessages(message);
    }

    @Transactional
    public void addToMessages(Message message){
        this.messages.add(message);
    }
}
