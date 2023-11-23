package com.deokma.library.repo;

import com.deokma.library.models.Books;
import com.deokma.library.models.Genre;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Denis Popolamov
 */
public interface BooksRepository extends CrudRepository<Books, Long> {
    List<Books> findByName(String name);
    List<Books> findByGenresIn(Set<Genre> genres);

    @Query(value = "SELECT DISTINCT b.* FROM books b " +
            "JOIN book_genre bg ON b.book_id = bg.book_id " +
            "JOIN genres g ON bg.genre_id = g.genre_id " +
            "WHERE g.genre_id IN :favoriteGenresIds AND b.book_id NOT IN :favoriteBooksIds", nativeQuery = true)
    List<Books> findByGenresInAndNotIn(@Param("favoriteGenresIds") List<Long> favoriteGenresIds,
                                       @Param("favoriteBooksIds") List<Long> favoriteBooksIds);


    //void deleteBooksByBook_id(long id);

}
