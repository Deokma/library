package com.deokma.library.services.impl;

import com.deokma.library.models.Books;
import com.deokma.library.models.Genre;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

        @Cacheable("paretoOptimalBooksCache")
        @Override
        public List<Books> findParetoOptimalBooks(Map<String, Object> criteria) {
            // Получаем критерии из параметров
            String name = ((String) criteria.get("name")).toLowerCase();
            String author = ((String) criteria.get("author")).toLowerCase();
            Float rating = parseFloating(criteria.get("rating"));
            Integer popularity = parseInteger(criteria.get("popularity"));
            Integer year = parseInteger(criteria.get("year"));
            Integer yearRance = parseInteger(criteria.get("yearRance"));
            Integer pageCount = parseInteger(criteria.get("pageCount"));
            Integer pageRance = parseInteger(criteria.get("pageRance"));
            // Получаем все книги из репозитория
            List<Books> allBooks = (List<Books>) booksRepository.findAll();

            // Применяем фильтры книг по переданным критериям
            List<Books> filteredBooks = allBooks.stream()
                    .filter(book -> (name == null || name.isEmpty() || isNameSimilar(name, book.getName())))
                    .filter(book -> (author == null || author.isEmpty() || isAuthorSimilar(author, book.getAuthor())))
                    .filter(book -> (rating == null || book.getAverageRating() >= rating))
                    .filter(book -> (popularity == null || book.getUserBookRatings().size() >= popularity))
                    .filter(book -> (year == null || isYearInRange(book.getIssueYear(), year,yearRance)))
                    .filter(book -> (pageCount == null || isPageCountInRange(book.getPageCount(), pageCount, pageRance)))
                    .collect(Collectors.toList());

            // Сортируем отфильтрованный список по мультипликативному критерию и популярности

            //Comparator<Books> popularityComparator = Comparator.comparingInt(book -> book.getUserBookRatings().size());
            Comparator<Books> multiplicativeComparator =
                    Comparator.comparingDouble(book -> calculateMultiplicativeCriteria(book, criteria));
            //Comparator<Books> popularityComparator = Comparator.comparingInt(book -> book.getUserBookRatings().size());

            // Сначала сортируем по мультипликативному критерию, затем по популярности
            filteredBooks.sort(Comparator.comparing(Books::getAverageRating).reversed().thenComparing(multiplicativeComparator.reversed()));
            // Первый этап обработки
            List<Books> firstStageResults = processFirstStage(filteredBooks, criteria);

            //List<Books> finalResults;

            if (pageCount == null) {
                // Если pageCount == null, выводим первый результат
                return firstStageResults;
            } else {
                // Иначе, выполняем второй этап обработки
                return processSecondStage(firstStageResults, pageCount);
            }
        }

    // Вместо точного сравнения рейтинга и популярности, используйте условия "больше или равно"
    private boolean isAuthorSimilar(String queryAuthor, String bookAuthor) {
        return Arrays.stream(queryAuthor.split("\\s+"))
                .anyMatch(queryWord -> Arrays.stream(bookAuthor.split("\\s+"))
                        .anyMatch(bookWord -> areAuthorsSimilar(queryWord, bookWord)));
    }

    private boolean isNameSimilar(String queryName, String bookName) {
        return Arrays.stream(queryName.split("\\s+"))
                .anyMatch(queryWord -> Arrays.stream(bookName.split("\\s+"))
                        .anyMatch(bookWord -> areNameSimilar(queryWord, bookWord)));
    }

    private List<Books> processFirstStage(List<Books> books, Map<String, Object> criteria) {
        String author = (String) criteria.get("author");
        List<Books> filteredBooksByAuthor = books.stream()
                .filter(book -> (author == null || author.isEmpty() || isAuthorSimilar(author, book.getAuthor())))
                .collect(Collectors.toList());

        // Сортируем отфильтрованный список по заданным критериям
        filteredBooksByAuthor.sort(Comparator.comparingDouble(book -> calculateDistance(book, criteria)));

        return filteredBooksByAuthor;
    }


    private double calculateDistance(Books book, Map<String, Object> criteria) {
        double distance = 0.0;
        // Получаем критерии из параметров
        String author = (String) criteria.get("author");
        Float rating = parseFloating(criteria.get("rating"));
        Integer popularity = parseInteger(criteria.get("popularity"));
        Integer year = parseInteger(criteria.get("year"));

        // Рассчитываем расстояние для каждого критерия и прибавляем к общему расстоянию
        if (author != null && !author.isEmpty()) {
            double authorDistance = calculateAuthorDistance(author, book.getAuthor());
            distance += authorDistance;
        }
        if (rating != null) {
            double ratingDistance = Math.abs(book.getAverageRating() - rating);
            distance += ratingDistance;
        }
        if (popularity != null) {
            double popularityDistance = Math.abs(book.getUserBookRatings().size() - popularity);
            distance += popularityDistance;
        }
        if (year != null) {
            double yearDistance = Math.abs(book.getIssueYear() - year);
            distance += yearDistance;
        }
        return distance;
    }

    private double calculateAuthorDistance(String queryAuthor, String bookAuthor) {
        // Используйте ваш алгоритм для расчета расстояния между авторами (может быть Levenshtein distance или другой)
        int levenshteinDistance = calculateLevenshteinDistance(queryAuthor, bookAuthor);
        return levenshteinDistance;
    }

    private boolean isYearInRange(Integer bookYear, Integer userYear, Integer yearRance) {
        // Предполагая, что userYear - это центральное значение, а диапазон - 3 года в каждую сторону
        yearRance = (yearRance == null) ? 3 : yearRance;
        int lowerBound = userYear - yearRance;
        int upperBound = userYear + yearRance;

        return (bookYear >= lowerBound && bookYear <= upperBound);
    }

    private boolean isPageCountInRange(Integer bookPageCount, Integer userPageCount, Integer pageRance) {
        // Предполагая, что userPageCount - это центральное значение, а диапазон - pageRance страниц в каждую сторону
        pageRance = (pageRance == null) ? 20 : pageRance;
        int lowerBound = userPageCount - pageRance;
        int upperBound = userPageCount + pageRance;

        return (bookPageCount >= lowerBound && bookPageCount <= upperBound);
    }

    private List<Books> processSecondStage(List<Books> books, int userPageCount) {
        // Сортируем книги по разнице между количеством страниц и указанным пользователем количеством страниц
        Comparator<Books> pageDifferenceComparator =
                Comparator.comparingInt(book -> Math.abs(book.getPageCount() - userPageCount));

        books.sort(pageDifferenceComparator);

        // Минимизируем разницу, выбирая топ-N книг
        int topN = Math.min(books.size(), 5); // Можно установить другое значение, если нужно больше или меньше книг
        List<Books> minimizedDifferenceBooks = books.subList(0, topN);

        return minimizedDifferenceBooks;
    }

    private Float parseFloating(Object value) {
        if (value instanceof String) {
            try {
                return Float.parseFloat((String) value);
            } catch (NumberFormatException | NullPointerException e) {
                //TODO Логирование ошибки
            }
        }
        return null;
    }

    private Integer parseInteger(Object value) {
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException | NullPointerException e) {
                //TODO Логирование ошибки
            }
        }
        return null;
    }

    // Пороговое значение для сравнения авторов
    private static final double AUTHOR_SIMILARITY_THRESHOLD = 0.6;

    private double calculateMultiplicativeCriteria(Books book, Map<String, Object> criteria) {
        double result = 1.0;

        // Получаем критерии из параметров
        String author = (String) criteria.get("author");
        Float rating = parseFloating(criteria.get("rating"));
        Integer popularity = parseInteger(criteria.get("popularity"));
        Integer year = parseInteger(criteria.get("year"));

        // Умножаем на соответствующий критерий, если он не является null
        if (author != null && !author.isEmpty()) {
            result *= areAuthorsSimilar(author, book.getAuthor()) ? AUTHOR_SIMILARITY_THRESHOLD : 0.0;
        }

        if (rating != null) {
            result *= book.getAverageRating() >= rating ? 1.0 : 0.0;
        }

        if (popularity != null) {
            result *= book.getUserBookRatings().size() >= popularity ? 1.0 : 0.0;
        }

        if (year != null) {
            result *= year.equals(book.getIssueYear()) ? 1.0 : 0.0;
        }

        return result;
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
        return distance <= 2;/*&& (1.0 - (double) distance / maxLength) >= SIMILARITY_THRESHOLD;*/
    }
        private boolean areNameSimilar(String name1, String name2) {
            int distance = calculateLevenshteinDistance(name1, name2);
            int maxLength = Math.max(name1.length(), name2.length());

            // Разрешить две ошибки
            return distance <= 2 && (1.0 - (double) distance / maxLength) >= SIMILARITY_THRESHOLD;
        }
}

