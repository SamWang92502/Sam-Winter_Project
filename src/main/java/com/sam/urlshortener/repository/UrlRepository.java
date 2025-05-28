// src/main/java/com/example/urlshortener/repository/UrlRepository.java
package com.sam.urlshortener.repository;
import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByCustomAlias(String customAlias);
    boolean existsByCustomAlias(String customAlias);
    List<UrlMapping> findByUser(User user); // Fetch URLs by user
    // New method to check if a user already used a custom alias
    boolean existsByUserAndCustomAlias(User user, String customAlias);
    // Optional: Fetch by user and alias (e.g., for redirect)
    Optional<UrlMapping> findByUserAndCustomAlias(User user, String customAlias);
}
