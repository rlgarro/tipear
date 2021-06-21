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

    @Column(name = "author")
    private String author;

    @Column(name = "category")
    private String category;

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return id + "-" +  content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
