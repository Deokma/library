package com.deokma.library.services;

import com.deokma.library.models.Books;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Denis Popolamov
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BooksRepository booksRepository;

    public void addBookToAccount(Long book_id) {
        Books books = booksRepository.findById(book_id).orElseThrow();
//        if (user != null) {
//            user.setActive(false);
//            log.info("Ban user with id = {}; name: {}", user.getId(), user.getUsername());
//        }
//        userRepository.save(user);

    }
}
