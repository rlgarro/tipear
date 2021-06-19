package com.roman.tipear.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "texts")
public class Texts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "text_id")
    private Long id;

    @Column(name = "content")
    private String content;

    public Long getId() {
        return id;
    }

    @Column(name = "title")
    private String title;

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return id + "-" +  content;
    }
}
