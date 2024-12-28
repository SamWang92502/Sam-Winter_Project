package urlshorterner;

public class UrlResponse {
    private String originalUrl;
    private String shortenedUrl;
    private String customAlias;

    // Constructor
    public UrlResponse(String originalUrl, String shortenedUrl, String customAlias) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.customAlias = customAlias;
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
}
