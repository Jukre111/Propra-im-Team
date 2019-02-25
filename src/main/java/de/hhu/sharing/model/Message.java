package de.hhu.sharing.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
public class Message {

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
