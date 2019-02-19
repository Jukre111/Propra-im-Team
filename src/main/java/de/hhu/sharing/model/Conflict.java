package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Conflict {

    @Id
    @GeneratedValue
    private Long id;

    private String problem;

    @ManyToOne
    private User prosecuter;

    @ManyToOne
    private User accused;

}
