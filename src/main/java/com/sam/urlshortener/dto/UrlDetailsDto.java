package com.sam.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlDetailsDto {
    private String shortUrl;          // The shortened URL
    private String longUrl;           // The original, long URL
    private int clickCount;           // Total clicks on the short URL
    private LocalDateTime lastAccessed; // Timestamp of the last access
    private LocalDateTime createdAt;  // Timestamp when the short URL was created

    // Constructors
    public UrlDetailsDto() {
    }

    public UrlDetailsDto(String shortUrl, String longUrl, int clickCount, LocalDateTime lastAccessed, LocalDateTime createdAt) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.clickCount = clickCount;
        this.lastAccessed = lastAccessed;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

