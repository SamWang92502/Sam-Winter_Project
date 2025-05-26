package com.sam.urlshortener.controller;
import com.sam.urlshortener.dto.ShortenRequest;
import com.sam.urlshortener.dto.UrlResponse;
import com.sam.urlshortener.model.UrlMapping;
import com.sam.urlshortener.model.User;
import com.sam.urlshortener.service.UrlService;
import com.sam.urlshortener.service.UserService;
import com.sam.urlshortener.dto.UrlDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    // Endpoint to shorten a URL
    // This maps the HTTP POST request to the URL path /shorten
    @PostMapping("/shorten")
    // @RequestParam String originalUrl: It expects a query/body parameter named originalUrl
    // @RequestParam(required = false) String customAlias:
    // This is an optional parameter — the user can provide a custom short alias if they want (short.ly/myalias).
    // If not provided, the system will auto-generate one.
    // @RequestHeader("Username") String username: The client must include a Username field in the HTTP headers.
    // This is how the server identifies which user is making the request.
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody ShortenRequest request,
                                                  @RequestHeader(value = "Username", required = false) String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Missing or invalid Username header");
        }
        User user = userService.getUserByUsername(username);

        UrlResponse response = urlService.shortenUrl(
                request.getOriginalUrl(),
                request.getCustomAlias(),
                user
        );
        return ResponseEntity.ok(response);
    }

    // Endpoint to retrieve the original URL and redirect
    @GetMapping("/redirect/{alias}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String alias) {
        String originalUrl = urlService.getOriginalUrl(alias);
        return ResponseEntity.status(302)
                .header("Location", originalUrl)
                .build();
    }

    // Endpoint to delete a shortened URL
    @DeleteMapping("/delete/{alias}")
    public ResponseEntity<String> deleteUrl(@PathVariable String alias,
                                            @RequestHeader("Username") String username) {
        User user = userService.getUserByUsername(username); // Fetch the logged-in user
        urlService.deleteUrl(alias, user);
        return ResponseEntity.ok("URL deleted successfully.");
    }

    // Endpoint to get all URLs for the logged-in user
    @GetMapping("/urls")
    public ResponseEntity<List<UrlMapping>> getUserUrls(@RequestHeader("Username") String username) {
        User user = userService.getUserByUsername(username); // Fetch the logged-in user
        List<UrlMapping> urls = urlService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

    // Endpoint to rename a shortened URL
    @PutMapping("/urls/rename")
    public ResponseEntity<String> renameShortUrl(@RequestParam String oldAlias,
                                                 @RequestParam String newAlias,
                                                 @RequestHeader("Username") String username) {
        User user = userService.getUserByUsername(username); // Fetch the logged-in user
        urlService.renameShortUrl(oldAlias, newAlias, user);
        return ResponseEntity.ok("Short URL renamed successfully.");
    }

    @PutMapping("/urls/edit")
    public ResponseEntity<String> editDestinationUrl(@RequestParam String shortUrl,
                                                     @RequestParam String newLongUrl,
                                                     @RequestHeader("Username") String username) {
        User user = userService.getUserByUsername(username); // Fetch the logged-in user
        urlService.editDestinationUrl(shortUrl, newLongUrl, user);
        return ResponseEntity.ok("Destination URL updated successfully.");
    }


    @GetMapping("/urls/analytics")
    public ResponseEntity<List<UrlDetailsDto>> getUserUrlsWithAnalytics(@RequestHeader("Username") String username) {
        User user = userService.getUserByUsername(username);
        List<UrlDetailsDto> urlsWithAnalytics = urlService.getUrlsWithAnalytics(user);
        return ResponseEntity.ok(urlsWithAnalytics);
    }

}
