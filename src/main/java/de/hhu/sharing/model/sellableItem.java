package de.hhu.sharing.model;

import javax.persistence.Entity;

@Entity (name = "sellableItem")
public class sellableItem extends Item{

    private Long price;

}
