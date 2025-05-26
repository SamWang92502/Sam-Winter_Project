package com.sam.urlshortener.service;

import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnalyticsService {

    private final UrlRepository urlRepository;

    public AnalyticsService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Record a click for the given short URL
    public void recordClick(String shortUrl) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            urlMapping.setHitCount(urlMapping.getHitCount() + 1); // Increment hit count
            urlMapping.setLastAccessed(LocalDateTime.now()); // Update last accessed timestamp
            urlRepository.save(urlMapping); // Save changes to the database
        } else {
            throw new IllegalArgumentException("Short URL not found.");
        }
    }

    // Get the total click count for a short URL
    public int getClickCount(String shortUrl) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        return (urlMapping != null) ? urlMapping.getHitCount() : 0;
    }

    // Get the last accessed time for a short URL
    public LocalDateTime getLastAccessed(String shortUrl) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        return (urlMapping != null) ? urlMapping.getLastAccessed() : null;
    }

    // Initialize analytics for a new short URL (optional if `hit_count` defaults to 0)
    public void initializeAnalytics(String shortUrl) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            urlMapping.setHitCount(0);
            urlMapping.setLastAccessed(null);
            urlRepository.save(urlMapping);
        }
    }
}
