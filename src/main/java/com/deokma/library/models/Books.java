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
    private String description;
    @ManyToMany(mappedBy = "books_list")
    private Set<User> users = new HashSet<>();

    @Transient
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

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
