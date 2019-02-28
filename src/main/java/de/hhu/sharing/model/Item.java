package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class Item {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    String name;

    @Column(columnDefinition = "TEXT")
    @NotNull
    String description;

    @OneToOne
    @Valid
    Image image;

    @ManyToOne
    @Valid
    User owner;
}
