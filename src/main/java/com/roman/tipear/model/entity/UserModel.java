package com.roman.tipear.model.entity;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true)
    @Basic(optional = false)
    @NotNull
    private String username;

    @NotNull
    @Column(name = "email", unique = true)
    @Basic(optional = false)
    private String email;

    @Column(name = "password")
    @Basic(optional = false)
    @NotNull
    private String password;

    @NotNull
    @OneToOne(mappedBy = "user")
    private TokenModel token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<TypingTest> tests;

    @Column(name= "activated", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean active = false;

    public UserModel(Long id, String username, String email, String password, TokenModel token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public UserModel() {
        super();
    }

    // getters
    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }
    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(Boolean activated) {
        this.active = activated;
    }

    public Boolean userIsActive() {
        return active;
    }

    public void setToken(TokenModel token) {
        this.token = token;
    }

    public TokenModel getToken() {
        return token;
    }
}