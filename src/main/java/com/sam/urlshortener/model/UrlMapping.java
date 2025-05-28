package com.sam.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "urls") // Maps this entity to the "urls" table
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 2048)
    @Column(name = "long_url", nullable = false)
    private String longUrl;

    /*
    @NotNull
    @Size(max = 255)
    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;
     */

    @Size(max = 255)
    @Column(name = "custom_alias")
    private String customAlias;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "hit_count", nullable = false)
    private int hitCount = 0; // Initialize hit count to 0

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user; // This links a URL mapping to a specific user

    // Default Constructor
    public UrlMapping() {}

    // Parameterized Constructor
    public UrlMapping(String longUrl, String customAlias, LocalDateTime createdAt, LocalDateTime expirationDate, User user) {
        this.longUrl = longUrl;
        //this.shortUrl = shortUrl;
        this.customAlias = customAlias;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    /*
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
     */

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed; // Getter for lastAccessed
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed; // Setter for lastAccessed
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "UrlMapping{" +
                "id=" + id +
                ", longUrl='" + longUrl + '\'' +
                ", customAlias='" + customAlias + '\'' +
                ", createdAt=" + createdAt +
                ", expirationDate=" + expirationDate +
                ", hitCount=" + hitCount +
                ", lastAccessed=" + lastAccessed +
                '}';
    }

    // Override equals and hashCode for meaningful comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlMapping that = (UrlMapping) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
