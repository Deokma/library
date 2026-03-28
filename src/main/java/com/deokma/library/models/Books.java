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
    //private String cover;
//    @OneToOne(cascade = CascadeType.ALL)
//    private BooksCover bookCover;

    @NotEmpty(message = "Author should not be empty")
    private String author;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @Column(length = 5000)
    private String description;
    @NotEmpty(message = "Page Count should not be empty")
    private Integer pageCount;
    private Float averageRating = 0.0f;

    private Integer issueYear;
    private Integer ratingCount = 0;
    @ManyToMany(mappedBy = "books_list")
    private Set<User> users = new HashSet<>();

    @Transient
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<UserBooksRating> userBookRatings = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

//    @Transient
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "book_id", referencedColumnName = "id")
//    private BooksPDF bookspdf;

    //    @Transient
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "book_id", referencedColumnName = "id")
//    private BooksCover bookscover;
    public Books() {

    }

}
