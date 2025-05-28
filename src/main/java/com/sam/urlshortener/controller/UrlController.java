package com.sam.urlshortener.controller;
import com.sam.urlshortener.dto.*;
import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.model.User;
import com.sam.urlshortener.service.UrlService;
import com.sam.urlshortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;
    private final UserService userService;

    @Autowired
    public UrlController(UrlService urlService, UserService userService) {
        this.urlService = urlService;
        this.userService = userService;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username);
    }


    // Endpoint to shorten a URL
    // This maps the HTTP POST request to the URL path /shorten
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody ShortenRequest request) {
        User user = null;
        try {
            user = getCurrentUser(); // Try to get logged-in user, if any
        } catch (Exception e) {
            // User is anonymous — leave user = null
        }

        UrlResponse response = urlService.shortenUrl(
                request.getOriginalUrl(),
                request.getCustomAlias(),
                user
        );
        return ResponseEntity.ok(response);
    }

    // Endpoint to delete a shortened URL
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteShortUrl(@RequestBody DeleteUrlRequest request) {
        User user = getCurrentUser(); // or use @AuthenticationPrincipal
        String deletedUrl = urlService.deleteUrl(request.getShortUrl(), user);
        return ResponseEntity.ok("\"shortUrl\": \"" + deletedUrl + "\" has been deleted successfully");
    }

    // Endpoint to get all URLs for the logged-in user
    @GetMapping("/urls")
    public ResponseEntity<List<UrlMapping>> getUserUrls() {
        User user = getCurrentUser();
        List<UrlMapping> urls = urlService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

    // Endpoint to rename a shortened URL
    @PatchMapping("/urls/id/{id}/custom-alias")
    public ResponseEntity<RenameAliasResponse> renameShortUrl(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body) {
        User user = getCurrentUser();
        String newAlias = body.get("customAlias");

        if (newAlias == null || newAlias.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        RenameAliasResponse response = urlService.renameShortUrlById(id, newAlias, user);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/urls/edit")
    public ResponseEntity<EditUrlResponse> editDestinationUrl(@RequestBody EditUrlRequest request) {
        User user = getCurrentUser(); // or use @AuthenticationPrincipal UserDetailsImpl
        EditUrlResponse response = urlService.editDestinationUrl(
                request.getShortUrl(),
                request.getNewLongUrl(),
                user
        );
        return ResponseEntity.ok(response);
    }



    @GetMapping("/urls/analytics")
    public ResponseEntity<List<UrlDetailsDto>> getUserUrlsWithAnalytics() {
        User user = getCurrentUser();
        List<UrlDetailsDto> urlsWithAnalytics = urlService.getUrlsWithAnalytics(user);
        return ResponseEntity.ok(urlsWithAnalytics);
    }

}
