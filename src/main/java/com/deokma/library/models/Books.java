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
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;
    private String cover;
    @NotEmpty(message = "Author should not be empty")
    private String author;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    private String description;
    @ManyToMany(mappedBy = "books_list")
    private Set<User> users = new HashSet<>();

    @Transient
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BooksPDF bookspdf;

    public Books(String name, String author, String cover, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.cover = cover;
    }

    public Books() {

    }

}
