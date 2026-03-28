package com.deokma.library.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genre_id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Books> books = new HashSet<>();

    public Genre() {
    }
}
