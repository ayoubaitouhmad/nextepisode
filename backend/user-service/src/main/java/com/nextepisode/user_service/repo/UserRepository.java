package com.nextepisode.user_service.repo;



import com.nextepisode.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}