package de.hhu.sharing.model;
import lombok.Data;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@Entity (name = "SellableItem")
public class SellableItem extends Item{

    @NotNull
    @Min(0)
    private Integer price;


    public SellableItem(){}

    public SellableItem(String name, String description, Integer price, User owner){
        super.name = name;
        super.description = description;
        this.price = price;
        super.owner = owner;
    }
    public SellableItem(String name, String description, Integer price, User owner, Image image){
        super.name = name;
        super.description = description;
        this.price = price;
        super.owner = owner;
        super.image = image;
    }
}
