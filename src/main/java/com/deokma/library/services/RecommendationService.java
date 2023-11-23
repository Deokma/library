package com.deokma.library.services;

import com.deokma.library.models.Books;
import com.deokma.library.models.Genre;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private BooksRepository booksRepository;

    public List<Books> getRecommendedBooks(User user) {
        // Получите список избранных книг пользователя
        List<Books> favoriteBooks = user.getBooks_list();

        // Получите все жанры этих книг
        Set<Genre> favoriteGenres = favoriteBooks.stream()
                .flatMap(book -> book.getGenres().stream())
                .collect(Collectors.toSet());

        // Получите идентификаторы жанров
        List<Long> favoriteGenresIds = favoriteGenres.stream()
                .map(Genre::getGenre_id)
                .collect(Collectors.toList());

        // Получите книги с похожими жанрами, исключив избранные книги
        List<Long> favoriteBooksIds = favoriteBooks.stream()
                .map(Books::getBook_id)
                .collect(Collectors.toList());

        List<Books> recommendedBooks = booksRepository.findByGenresInAndNotIn(favoriteGenresIds, favoriteBooksIds);

        return recommendedBooks;
    }

}
