// src/main/java/com/example/urlshortener/repository/UrlRepository.java
package com.sam.urlshortener.repository;
import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(User user); // Fetch URLs by user
}
