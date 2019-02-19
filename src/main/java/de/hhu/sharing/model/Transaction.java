package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue
    int reservationId;
    int wholeRent;
    int deposit;

    @ManyToOne
    Item item;

    @ManyToOne
    User source;

    @ManyToOne
    User target;
}
