package com.example.virtuallibrary.models;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique=true)    
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 5 and 20 characters")    
    private String username;

    @Column(nullable = false)    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")    
    private String password;

    @OneToMany(mappedBy = "user")
    private List<BookCheckout> checkouts; 

    @ManyToMany(fetch = FetchType.EAGER)   
    private List<Role> roles;
        
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<BookCheckout> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(List<BookCheckout> checkouts) {
        this.checkouts = checkouts;
    }


}
