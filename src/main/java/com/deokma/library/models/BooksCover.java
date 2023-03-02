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
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "original_file_name")
    private String originalFileName;
//    @OneToOne(mappedBy = "bookscover")
//     private Books books;

}
