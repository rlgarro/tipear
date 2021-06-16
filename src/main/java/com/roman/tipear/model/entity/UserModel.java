package com.roman.tipear.model.entity;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Component
@Entity
@Table(name = "users")
public class UserModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
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

    public UserModel(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
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

    public Boolean userIsActive() {
        return true;
    }
}