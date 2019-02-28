package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String mimeType;


    private byte[] imageData;


    public void setImageData(byte[] imageData) {
        this.imageData = Arrays.copyOf(imageData, imageData.length);
    }

    public byte[] getImageData(){
        return (byte[])imageData.clone();
    }

}
