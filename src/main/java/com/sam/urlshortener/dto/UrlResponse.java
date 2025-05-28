package com.sam.urlshortener.dto;

public class UrlResponse {
    private String originalUrl;
    private String shortenedUrl;
    private String customAlias;
    private String qrCodeBase64;

    // Constructor
    public UrlResponse(String originalUrl, String shortenedUrl, String customAlias, String qrCodeBase64) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.customAlias = customAlias;
        this.qrCodeBase64 = qrCodeBase64;
    }

    // Getters and Setters
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

}
