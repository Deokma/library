package com.deokma.library.repo;

import com.deokma.library.models.Books;
import com.deokma.library.models.User;
import com.deokma.library.models.UserBooksRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBooksRatingRepository extends CrudRepository<UserBooksRating, Long> {

    Optional<UserBooksRating> findByUserAndBook(User user, Books book);

    List<UserBooksRating> findByBook(Books book);
}
