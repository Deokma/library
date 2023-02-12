package com.deokma.library.services;

import com.deokma.library.exceptions.ResourceNotFoundException;
import com.deokma.library.models.BooksPDF;
import com.deokma.library.repo.BooksPdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Denis Popolamov
 */
@Service
public class BooksPDFService {

    private final BooksPdfRepository bookPdfRepository;

    @Autowired
    public BooksPDFService(BooksPdfRepository bookPdfRepository) {
        this.bookPdfRepository = bookPdfRepository;
    }

    public BooksPDF getBookPdfById(Long id) {
        return bookPdfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BookPdf not found with id: " + id));
    }
}