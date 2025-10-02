package com.gabrielrodrigues.encurtador_de_links.models;

import jakarta.persistence.*;

@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String shortUrl;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private int clicks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Link() {}

    public Link(String shortUrl, String originalUrl, User user) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.clicks = 0;
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return this.shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public int getClicks() {
        return this.clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public User getUser() {
        return this.user;
    }
}
