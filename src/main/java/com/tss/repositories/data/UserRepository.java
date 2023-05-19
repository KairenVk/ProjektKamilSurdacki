package com.tss.repositories.data;

import com.tss.entities.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    boolean existsById(Long id);

    Optional<User> findByUsername(String username);
}