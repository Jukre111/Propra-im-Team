package de.hhu.sharing.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;
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
    private User lender;

    @ManyToOne
    private User borrower;

    @OneToOne
    private BorrowingProcess process;

    @ElementCollection
    private List<Message> messages = new ArrayList<>();

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
