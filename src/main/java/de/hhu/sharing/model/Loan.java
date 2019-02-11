package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity

public class Loan {

    @Id
    @GeneratedValue
    private Long id;

}
