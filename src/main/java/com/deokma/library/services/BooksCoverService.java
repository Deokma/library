package com.deokma.library.services;

import com.deokma.library.exceptions.ResourceNotFoundException;
import com.deokma.library.models.BooksCover;
import com.deokma.library.repo.BooksCoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Denis Popolamov
 */
@Service
public class BooksCoverService {

    private final BooksCoverRepository bookCoverRepository;

    @Autowired
    public BooksCoverService(BooksCoverRepository bookCoverRepository) {
        this.bookCoverRepository = bookCoverRepository;
    }

    public BooksCover getBookCoverById(Long id) {
        return bookCoverRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BookCover not found with id: " + id));
    }
}