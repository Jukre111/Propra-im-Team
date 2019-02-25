package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BorrowingProcess {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private lendableItem lendableItem;

    @Embedded
    private Period period;

    public BorrowingProcess(){
    }

    public BorrowingProcess(lendableItem lendableItem, Period period){
        this.lendableItem = lendableItem;
        this.period = period;
    }

}
