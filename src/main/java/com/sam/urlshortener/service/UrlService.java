package com.sam.urlshortener.service;
import com.sam.urlshortener.dto.EditUrlResponse;
import com.sam.urlshortener.util.QrCodeUtil;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.sam.urlshortener.dto.UrlResponse;
import com.sam.urlshortener.dto.UrlDetailsDto;
import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.model.User;
import com.sam.urlshortener.repository.UrlRepository;
import com.sam.urlshortener.dto.RenameAliasResponse;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final AnalyticsService analyticsService;
    private final EmailSenderService emailSenderService;

    // Constructor injection for dependencies
    public UrlService(
            UrlRepository urlRepository,
            AnalyticsService analyticsService,
            EmailSenderService emailSenderService
    ) {
        this.urlRepository = urlRepository;
        this.analyticsService = analyticsService; // Inject AnalyticsService
        this.emailSenderService = emailSenderService;
    }

    // Method to shorten a URL and associate it with a user
    public UrlResponse shortenUrl(String longUrl, String customAlias, User user) {
        String alias = (customAlias != null && !customAlias.isEmpty())
                //if yes
                ? customAlias // Ternary operator: shortcut for if-else in Java
                //if no
                : UUID.randomUUID().toString().substring(0, 6); // Generate random short URL
                // UUID: Universally Unique Identifier

        if (urlRepository.findByCustomAlias(alias) != null) {
            throw new IllegalArgumentException("Alias already exists.");
        }

        // Save the URL mapping
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setCustomAlias(alias);
        urlMapping.setCreatedAt(LocalDateTime.now()); //current timestamp
        urlMapping.setHitCount(0);
        urlMapping.setUser(user); // Associate URL with the user
        //Saves the new record to the database using Spring Data JPA.
        urlRepository.save(urlMapping);
        String fullShortUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{alias}")
                .buildAndExpand(alias)
                .toUriString();

        if (user != null && user.getEmail() != null) {
            emailSenderService.sendShortUrlEmail(user.getEmail(), fullShortUrl);
        }

        String qrCodeBase64;
        try {
            qrCodeBase64 = QrCodeUtil.generateQRCodeBase64(fullShortUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }

        return new UrlResponse(longUrl, fullShortUrl, alias, qrCodeBase64);
    }

    // Method to get the original long URL by short URL
    public String getOriginalUrl(String alias) {
        UrlMapping urlMapping = urlRepository.findByCustomAlias(alias);
        if (urlMapping == null) {
            throw new IllegalArgumentException("Short URL not found.");
        }
        return urlMapping.getLongUrl();
    }


    public RenameAliasResponse renameShortUrlById(Long id, String newAlias, User user) {
        UrlMapping urlMapping = urlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("URL not found."));

        if (!urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("This URL does not belong to the current user.");
        }

        if (urlRepository.findByCustomAlias(newAlias) != null) {
            throw new IllegalArgumentException("The new alias is already in use.");
        }

        String oldAlias = urlMapping.getCustomAlias();
        urlMapping.setCustomAlias(newAlias);
        urlRepository.save(urlMapping);

        String fullShortUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{alias}")
                .buildAndExpand(newAlias)
                .toUriString();

        return new RenameAliasResponse(fullShortUrl, urlMapping.getLongUrl());
    }

    // Method to delete a URL mapping
    public String deleteUrl(String fullShortUrl, User user) {
        // Extract the alias from the full short URL
        String alias = fullShortUrl.substring(fullShortUrl.lastIndexOf("/") + 1);

        UrlMapping urlMapping = urlRepository.findByCustomAlias(alias);
        if (urlMapping == null || !urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("Short URL not found or does not belong to the user.");
        }
        urlRepository.delete(urlMapping);
        return fullShortUrl; // return the full URL that was deleted
    }

    // Method to get all URLs for a specific user
    public List<UrlMapping> getUrlsByUser(User user) {
        return urlRepository.findByUser(user);
    }

    public void renameShortUrl(String oldAlias, String newAlias, User user) {
        // Fetch the existing URL mapping
        UrlMapping urlMapping = urlRepository.findByCustomAlias(oldAlias);
        if (urlMapping == null || !urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("Short URL not found or does not belong to the user.");
        }

        // Check if the new alias already exists
        if (urlRepository.findByCustomAlias(newAlias) != null) {
            throw new IllegalArgumentException("The new alias is already in use.");
        }

        // Update the alias
        urlMapping.setCustomAlias(newAlias);
        urlRepository.save(urlMapping);
    }

    public EditUrlResponse editDestinationUrl(String fullShortUrl, String newLongUrl, User user) {
        // Extract alias from full URL
        String alias = fullShortUrl.substring(fullShortUrl.lastIndexOf("/") + 1);

        // Fetch the existing URL mapping
        UrlMapping urlMapping = urlRepository.findByCustomAlias(alias);
        if (urlMapping == null || !urlMapping.getUser().equals(user)) {
            throw new IllegalArgumentException("Short URL not found or does not belong to the user.");
        }

        // Update the destination URL
        urlMapping.setLongUrl(newLongUrl);
        urlRepository.save(urlMapping);

        return new EditUrlResponse(fullShortUrl, newLongUrl);
    }


    public List<UrlDetailsDto> getUrlsWithAnalytics(User user) {
        List<UrlMapping> urlMappings = urlRepository.findByUser(user);
        return urlMappings.stream().map(url -> {
            UrlDetailsDto dto = new UrlDetailsDto();
            dto.setShortUrl(url.getCustomAlias());
            dto.setLongUrl(url.getLongUrl());
            dto.setClickCount(analyticsService.getClickCount(url.getCustomAlias())); // Use injected instance
            dto.setLastAccessed(analyticsService.getLastAccessed(url.getCustomAlias())); // Use injected instance
            dto.setCreatedAt(url.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }


}
