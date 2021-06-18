package com.roman.tipear.model.entity;

import com.sun.istack.NotNull;
import jdk.jfr.Timestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tests")
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

    private LocalDateTime takenAt;

    public TypingTest(UserModel user, int score) {
        this.user = user;
        this.takenAt = LocalDateTime.now();
        this.score = score;
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

    public LocalDateTime takenAt() {
        return takenAt;
    }

}
