package com.sam.urlshortener.repository;

import com.sam.urlshortener.model.User;
//JpaRepository automatically implements SQL operations for you
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// UserRepository is a repository interface for managing User entities.
// User is the entity type.
// Long is the type of the primary key (usually the id in your User entity).
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
