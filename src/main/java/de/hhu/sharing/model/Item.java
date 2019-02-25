package de.hhu.sharing.model;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

public class Item {


    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private Image image;

    @ManyToOne
    private User owner;
}
