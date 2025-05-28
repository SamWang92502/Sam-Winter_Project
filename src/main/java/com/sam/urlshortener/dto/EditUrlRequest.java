package com.sam.urlshortener.dto;

public class EditUrlRequest {
    private String shortUrl;
    private String newLongUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getNewLongUrl() {
        return newLongUrl;
    }

    public void setNewLongUrl(String newLongUrl) {
        this.newLongUrl = newLongUrl;
    }
}
