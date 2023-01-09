package com.deokma.library.repo;

import com.deokma.library.models.Books;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Denis Popolamov
 */
public interface BooksRepository extends CrudRepository<Books, Long> {

}
