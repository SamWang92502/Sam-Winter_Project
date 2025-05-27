package com.sam.urlshortener.service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.sam.urlshortener.dto.UrlResponse;
import com.sam.urlshortener.dto.UrlDetailsDto;
import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.model.User;
import com.sam.urlshortener.repository.UrlRepository;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final AnalyticsService analyticsService;

    // Constructor injection for dependencies
    public UrlService(UrlRepository urlRepository, AnalyticsService analyticsService) {
        this.urlRepository = urlRepository;
        this.analyticsService = analyticsService; // Inject AnalyticsService
    }

    // Method to shorten a URL and associate it with a user
    public UrlResponse shortenUrl(String longUrl, String customAlias, User user) {
        String shortUrl = (customAlias != null && !customAlias.isEmpty())
                //if yes
                ? customAlias // Ternary operator: shortcut for if-else in Java
                //if no
                : UUID.randomUUID().toString().substring(0, 6); // Generate random short URL
                // UUID: Universally Unique Identifier

        // Check if custom alias already exists
        if (customAlias != null && urlRepository.findByShortUrl(customAlias) != null) {
            throw new IllegalArgumentException("Custom alias already exists.");
        }

        // Save the URL mapping
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCustomAlias(customAlias);
        urlMapping.setCreatedAt(LocalDateTime.now()); //current timestamp
        urlMapping.setHitCount(0);
        urlMapping.setUser(user); // Associate URL with the user
        //Saves the new record to the database using Spring Data JPA.
        urlRepository.save(urlMapping);
        String fullShortUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{alias}")
                .buildAndExpand(shortUrl)
                .toUriString();

        // Return the response
        // Returns a DTO containing the values the frontend or client can use/display.
        return new UrlResponse(longUrl, fullShortUrl, customAlias);
    }

    // Method to get the original long URL by short URL
    public String getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            throw new IllegalArgumentException("Short URL not found.");
        }
        return urlMapping.getLongUrl();
    }

    // Method to delete a URL mapping
    public void deleteUrl(String shortUrl, User user) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        if (urlMapping == null || !urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("Short URL not found or does not belong to the user.");
        }
        urlRepository.delete(urlMapping);
    }

    // Method to get all URLs for a specific user
    public List<UrlMapping> getUrlsByUser(User user) {
        return urlRepository.findByUser(user);
    }

    public void renameShortUrl(String oldAlias, String newAlias, User user) {
        // Fetch the existing URL mapping
        UrlMapping urlMapping = urlRepository.findByShortUrl(oldAlias);
        if (urlMapping == null || !urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("Short URL not found or does not belong to the user.");
        }

        // Check if the new alias already exists
        if (urlRepository.findByShortUrl(newAlias) != null) {
            throw new IllegalArgumentException("The new alias is already in use.");
        }

        // Update the alias
        urlMapping.setShortUrl(newAlias);
        urlRepository.save(urlMapping);
    }

    public void editDestinationUrl(String shortUrl, String newLongUrl, User user) {
        // Fetch the existing URL mapping
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        if (urlMapping == null || !urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("Short URL not found or does not belong to the user.");
        }

        // Update the destination URL
        urlMapping.setLongUrl(newLongUrl);
        urlRepository.save(urlMapping);
    }


    public List<UrlDetailsDto> getUrlsWithAnalytics(User user) {
        List<UrlMapping> urlMappings = urlRepository.findByUser(user);
        return urlMappings.stream().map(url -> {
            UrlDetailsDto dto = new UrlDetailsDto();
            dto.setShortUrl(url.getShortUrl());
            dto.setLongUrl(url.getLongUrl());
            dto.setClickCount(analyticsService.getClickCount(url.getShortUrl())); // Use injected instance
            dto.setLastAccessed(analyticsService.getLastAccessed(url.getShortUrl())); // Use injected instance
            dto.setCreatedAt(url.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }


}
