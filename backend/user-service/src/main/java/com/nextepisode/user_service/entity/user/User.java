package com.nextepisode.user_service.entity.user;


import com.nextepisode.user_service.entity.tv.Tv;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
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
    private String profileVisibility = "public"; // public, private, friends


//    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(
//            name = "user_tvs",
//            joinColumns = @JoinColumn(name = "tv_id"),
//            inverseJoinColumns = @JoinColumn(name = "genre_id")
//    )
//    private List<Tv> tvs = new ArrayList<>();


}
