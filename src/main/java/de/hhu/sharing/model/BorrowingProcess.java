package de.hhu.sharing.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;

@Data
@Entity
@ToString(exclude = "item")
public class BorrowingProcess {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Valid
    private LendableItem item;

    @Embedded
    @Valid
    private Period period;

    public BorrowingProcess(){
    }

    public BorrowingProcess(LendableItem item, Period period){
        this.item = item;
        this.period = period;
    }

}
