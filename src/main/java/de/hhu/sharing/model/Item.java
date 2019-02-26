package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class Item {

    @Id
    @GeneratedValue
    Long id;

    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @OneToOne
    Image image;

    @ManyToOne
    User owner;
}
