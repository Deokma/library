package com.deokma.library.services;

import com.deokma.library.models.Genre;
import com.deokma.library.repo.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return (List<Genre>) genreRepository.findAll();
    }

    // Другие методы сервиса, если необходимо
}
