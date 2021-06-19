package com.roman.tipear.model.entity;

import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tokens")
@Component
public class RegTokenModel {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token", unique = true)
    private String token;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserModel user;

    @Column(name = "created_at")
    @NotNull
    private LocalDate createdAt;

    @Column(name = "expires_at")
    @NotNull
    private LocalDate expiresAt;


    public RegTokenModel(String token, UserModel user) {
        this.user = user;
        this.token = token;
        this.createdAt = LocalDate.now();
        this.expiresAt = createdAt.plusDays(1);
    }

    public RegTokenModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RegTokenModel{" +
                "id=" + id +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}