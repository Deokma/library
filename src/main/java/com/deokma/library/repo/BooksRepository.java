package com.deokma.library.repo;

import com.deokma.library.models.Books;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Denis Popolamov
 */
public interface BooksRepository extends CrudRepository<Books, Long> {
    List<Books> findByName(String name);
    //void deleteBooksByBook_id(long id);

}
