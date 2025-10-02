package com.gabrielrodrigues.encurtador_de_links.models;

import jakarta.persistence.*;

@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String short_url;

    @Column(nullable = false, unique = true)
    private String original_url;

    @Column(nullable = false, unique = true)
    private int clicks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Link() {}

    public Link(String short_url, String original_url, int clicks, User user) {
        this.short_url = short_url;
        this.original_url = original_url;
        this.clicks = 0;
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShort_url() {
        return this.short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public String getOriginal_url() {
        return this.original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
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
