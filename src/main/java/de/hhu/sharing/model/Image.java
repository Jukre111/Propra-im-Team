package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;
    
    private String mimeType;


    private byte[] imageData;
}
