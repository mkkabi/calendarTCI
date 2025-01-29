package com.mkkabi.dev.repository;

import com.mkkabi.dev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where email =?1", nativeQuery = true)
    Optional<User> getUserByEmail(String email);
}
