package com.nextepisode.user_service.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


//public class User {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String username;
//
//    @Column(nullable = false)
//    private String password; // stored hashed
//
//    @Column(unique = true)
//    private String email;
//
//    private String role = "USER";
//}


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private String avatar;
    private String role = "USER";
    private Boolean emailVerified = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private Boolean isActive = true;

    // Additional profile fields
    private String bio;
    private String location;
    private String website;
    private String phone;
    private LocalDate dateOfBirth;
    private String preferredLanguage;
    private String timezone;
    private Boolean notificationsEnabled = true;
    private String profileVisibility = "public"; // public, private, friends




}
