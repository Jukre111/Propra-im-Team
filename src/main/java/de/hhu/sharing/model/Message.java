package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
public class Message {

    @NotNull
    private String author;
    @Column(columnDefinition = "TEXT")
    private String content;

    public Message(){
    }

    public Message(String author, String content){
        this.author = author;
        this.content = content;
    }
}
