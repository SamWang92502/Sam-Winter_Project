package com.example.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // Endpoint to shorten a URL
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestParam String originalUrl,
                                                  @RequestParam(required = false) String customAlias) {
        // Now we call urlService.shortenUrl(...)
        UrlResponse response = urlService.shortenUrl(originalUrl, customAlias);
        return ResponseEntity.ok(response);
    }

    // Endpoint to retrieve the original URL and redirect
    @GetMapping("/redirect/{alias}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String alias) {
        String originalUrl = urlService.getOriginalUrl(alias);
        // Return a 302 (Found) redirect
        return ResponseEntity.status(302)
                .header("Location", originalUrl)
                .build();
    }

    // Endpoint to delete a shortened URL
    @DeleteMapping("/delete/{alias}")
    public ResponseEntity<String> deleteUrl(@PathVariable String alias) {
        urlService.deleteUrl(alias);
        return ResponseEntity.ok("URL deleted successfully.");
    }
}
