package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER)
    private User receiver;
    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;
    private String subject;
    private String message;
}
