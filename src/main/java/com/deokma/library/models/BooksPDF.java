package com.deokma.library.models;

import jakarta.persistence.*;


/**
 * @author Denis Popolamov
 */
@Entity
@Table(name = "bookspdf")
public class BooksPDF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    @Lob
    private byte[] data;
    @OneToOne(mappedBy = "bookspdf")
    private Books books;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
