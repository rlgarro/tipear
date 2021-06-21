package com.roman.tipear.model.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "user_tests")
public class TypingTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "test_id")
    private Long testId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserModel user;

    @Column(name = "score")
    private int score;

    @Column(name = "text_id")
    private Long textId;

    private String author;

    private String title;

    public TypingTest(UserModel user, int score, Long textId, String author, String title) {
        this.user = user;
        this.score = score;
        this.textId = textId;
        this.author = author;
        this.title = title;
    }

    public TypingTest() {

    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getTextId() {
        return textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String text) {
        this.author = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }
}