package com.soul.calendar.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Users")
public class Users {

    public Users() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer Id;

    @NotNull
    @Column(name = "name")
    @Size(min = 2, max = 30, message = "Length of Name must be greater than 2")
    private String name;

    @Email(message = "Please enter a valid email address")
    @Column(name = "email", unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp CreatedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private Timestamp updatedAt;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        CreatedAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Users(Integer id, String name, String email, Timestamp createdAt, Timestamp updatedAt) {
        Id = id;
        this.name = name;
        this.email = email;
        CreatedAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Users [Id=" + Id + ", name=" + name + ", email=" + email + ", CreatedAt=" + CreatedAt + ", updatedAt="
                + updatedAt + "]";
    }

}
