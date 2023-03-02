package com.deokma.library.repo;

import com.deokma.library.models.BooksCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Denis Popolamov
 */
public interface BooksCoverRepository extends JpaRepository<BooksCover, Long> {
    @Modifying
    @Query(value = "CREATE TEMPORARY TABLE temp_book_cover_image AS (SELECT id, 'data' FROM bookscovers)", nativeQuery = true)
    void createTempTable();
    BooksCover deleteByFileName(String name);
}