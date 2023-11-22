package com.deokma.library.services.impl;

import com.deokma.library.models.Books;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Books> findParetoOptimalBooks(Map<String, Object> criteria) {
        // Получаем критерии из параметров
        String author = (String) criteria.get("author");
        Float rating = parseFloating(criteria.get("rating"));
        Integer popularity = parseInteger(criteria.get("popularity"));
        Integer year = parseInteger(criteria.get("year"));

        // Получаем все книги из репозитория
        List<Books> allBooks = (List<Books>) booksRepository.findAll();

        // Применяем фильтры книг по переданным критериям
        List<Books> filteredBooks = allBooks.stream()
                .filter(book -> (author == null || author.isEmpty() || isAuthorSimilar(author, book.getAuthor())))
                .filter(book -> (rating == null || rating.equals(book.getAverageRating())))
                .filter(book -> (popularity == null || popularity.equals(book.getUserBookRatings().size())))
                .filter(book -> (year == null || year.equals(book.getIssueYear())))
                .collect(Collectors.toList());

        // Сортируем отфильтрованный список по каждому критерию
        Comparator<Books> authorComparator = Comparator.comparing(Books::getAuthor);
        Comparator<Books> ratingComparator = Comparator.comparing(Books::getAverageRating);
        Comparator<Books> popularityComparator = Comparator.comparingInt(book -> book.getUserBookRatings().size());
        Comparator<Books> yearComparator = Comparator.comparing(Books::getIssueYear);

        filteredBooks.sort(authorComparator.thenComparing(ratingComparator).thenComparing(popularityComparator).thenComparing(yearComparator));

        return filteredBooks;
    }

    private boolean isAuthorSimilar(String queryAuthor, String bookAuthor) {
        return Arrays.stream(queryAuthor.split("\\s+"))
                .anyMatch(queryWord -> Arrays.stream(bookAuthor.split("\\s+"))
                        .anyMatch(bookWord -> areAuthorsSimilar(queryWord, bookWord)));
    }

    private Float parseFloating(Object value) {
        if (value instanceof String) {
            try {
                return Float.parseFloat((String) value);
            } catch (NumberFormatException | NullPointerException e) {
                // Логирование ошибки, если необходимо
            }
        }
        return null;
    }

    private Integer parseInteger(Object value) {
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException | NullPointerException e) {
                // Логирование ошибки, если необходимо
            }
        }
        return null;
    }


    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }


    private static final double SIMILARITY_THRESHOLD = 0.8; // Adjust as needed

    private boolean areAuthorsSimilar(String author1, String author2) {
        int distance = calculateLevenshteinDistance(author1, author2);
        int maxLength = Math.max(author1.length(), author2.length());

        // Разрешить две ошибки
        return distance <= 2 && (1.0 - (double) distance / maxLength) >= SIMILARITY_THRESHOLD;
    }
}

