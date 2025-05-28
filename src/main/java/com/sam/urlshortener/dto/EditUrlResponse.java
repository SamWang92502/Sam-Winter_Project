package com.sam.urlshortener.dto;

public class EditUrlResponse {
    private String shortUrl;
    private String longUrl;

    public EditUrlResponse(String shortUrl, String longUrl) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
