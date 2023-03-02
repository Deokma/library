package com.deokma.library.models;

import jakarta.persistence.*;
import lombok.Data;


/**
 * @author Denis Popolamov
 */
@Data
@Entity
@Table(name = "bookspdf")
public class BooksPDF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "origin_file_name")
    private String originalFileName;


}
