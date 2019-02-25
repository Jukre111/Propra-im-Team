package de.hhu.sharing.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@ToString(exclude = "item")
public class BorrowingProcess {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Item item;

    @Embedded
    private Period period;

    public BorrowingProcess(){
    }

    public BorrowingProcess(Item item, Period period){
        this.item = item;
        this.period = period;
    }

}
