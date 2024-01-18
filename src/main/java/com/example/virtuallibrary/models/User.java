package com.example.virtuallibrary.models;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique=true)    
    private String username;

    @Column(nullable = false)    
    private String password;

    @OneToMany(mappedBy = "currentUser") // Use @OneToMany in User
    private List<Book> checkouts; // Reference the join entity

    @ManyToMany(fetch = FetchType.EAGER)   
    private Collection<Role> roles;
        
    private boolean enabled;    

    public User() {
        super();
        this.enabled = true;
    }

    public User(String username, String password) {
        super();
        this.username = username.toLowerCase();
        this.password = password;
        this.enabled = true;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }



}
