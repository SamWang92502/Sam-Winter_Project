package urlshorterner;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Method to shorten a URL (renamed from 'generateShortUrl' to 'shortenUrl')
    public UrlResponse shortenUrl(String longUrl, String customAlias) {
        String shortUrl = (customAlias != null && !customAlias.isEmpty())
                ? customAlias
                : UUID.randomUUID().toString().substring(0, 6); // Generate random short URL

        // Check if custom alias already exists
        if (customAlias != null && urlRepository.findByShortUrl(customAlias) != null) {
            throw new IllegalArgumentException("Custom alias already exists.");
        }

        // Save the URL mapping
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCustomAlias(customAlias);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setHitCount(0);
        urlRepository.save(urlMapping);

        // Return the response
        return new UrlResponse(longUrl, shortUrl, customAlias);
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
    public void deleteUrl(String shortUrl) {
        UrlMapping urlMapping = urlRepository.findByShortUrl(shortUrl);
        if (urlMapping == null) {
            throw new IllegalArgumentException("Short URL not found.");
        }
        urlRepository.delete(urlMapping);
    }
}
