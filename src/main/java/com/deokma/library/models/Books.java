package com.deokma.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Denis Popolamov
 */
@Entity
@Data
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long book_id;
    private String cover;
    private String view_link;
    private String download_link;
    @NotEmpty(message = "Author should not be empty")
    private String author;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    private String description;
    @ManyToMany(mappedBy = "books_list")
    private Set<User> users = new HashSet<>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    public Books(String name, String author, String cover, String view_link, String download_link, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.view_link = view_link;
        this.download_link = download_link;
        this.cover = cover;
    }

    public Books() {

    }

}
