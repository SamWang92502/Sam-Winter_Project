package com.sam.urlshortener.dto;

public class RenameAliasResponse {
    private String shortUrl;
    private String longUrl;

    public RenameAliasResponse(String shortUrl, String longUrl) {
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
