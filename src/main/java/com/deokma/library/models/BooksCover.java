package com.deokma.library.models;

import jakarta.persistence.*;
import lombok.Data;


/**
 * @author Denis Popolamov
 */
@Data
@Entity
@Table(name = "bookscovers")
public class BooksCover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    @Lob
    private byte[] data;
    //@OneToOne(mappedBy = "bookspdf")
    // private Books books;

}
