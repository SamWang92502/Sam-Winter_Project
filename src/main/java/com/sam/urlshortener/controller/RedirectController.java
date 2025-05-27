package com.sam.urlshortener.controller;

import com.sam.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedirectController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/{alias}")
    public ResponseEntity<Void> redirect(@PathVariable String alias) {
        String originalUrl = urlService.getOriginalUrl(alias);
        return ResponseEntity.status(302)
                .header("Location", originalUrl)
                .build();
    }
}
