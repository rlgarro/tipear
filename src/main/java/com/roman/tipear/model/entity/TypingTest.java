package com.roman.tipear.model.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tests")
public class TypingTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserModel user;

    @Column(name = "score")
    private int score;

    @Column(name = "text_id")
    private Long textId;

    private String text;

    private String title;

    public TypingTest(UserModel user, int score, Long textId, String tex, String title) {
        this.user = user;
        this.score = score;
        this.textId = textId;
        this.text = text;
        this.title = title;
    }

    public TypingTest() {

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
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

}
