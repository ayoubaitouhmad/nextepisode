package com.nextepisode.user_service.entity.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;


    private String avatar;
    private String role = "USER";
    private Boolean emailVerified = false;
    private Instant createdAt;
    private Instant updatedAt;
    private LocalDateTime lastLogin;
    private Boolean isActive = true;
    private Boolean isDirty = false;

    // Additional profile fields
    private String bio;
    private String location;
    private String website;
    private String phone;
    private LocalDate dateOfBirth;
    private String preferredLanguage;
    private String timezone;
    private Boolean notificationsEnabled = true;
    private Boolean profileVisibility = false;
    private Boolean activitySharing = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }



}
