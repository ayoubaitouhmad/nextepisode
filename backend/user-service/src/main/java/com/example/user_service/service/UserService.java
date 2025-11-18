package com.example.user_service.service;

import com.example.user_service.entity.User;
import com.example.user_service.repo.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;



    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional()
    public User create() {
        User user = new User();
        user.setEmail("First@dffd.com");
        user.setFirstName("First");
        user.setLastName("Last");
        return repo.save(user);
    }
//
//    @Transactional
//    public User updateProfile(String username, UpdateProfileRequest body) {
//        var u = getOrCreate(username);
//        if (body.displayName() != null) u.setDisplayName(body.displayName());
//        if (body.bio() != null) u.setBio(body.bio());
//        return repo.save(u);
//    }
}
