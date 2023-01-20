package com.deokma.library.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Denis Popolamov
 */
@Entity
@Data
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long book_id;
    private int genre_id;
    private String cover, view_link, download_link, author, name, description;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    public Books() {
    }

    public Books(String name, String author, String cover, String view_link, String download_link, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.view_link = view_link;
        this.download_link = download_link;
        this.cover = cover;
    }
}
