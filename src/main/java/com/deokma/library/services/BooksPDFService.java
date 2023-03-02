package com.deokma.library.services;

import com.deokma.library.exceptions.ResourceNotFoundException;
import com.deokma.library.models.BooksPDF;
import com.deokma.library.repo.BooksPdfRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private EntityManager entityManager;
    @Transactional
    public void deleteByFileName(String fileName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<BooksPDF> query = builder.createCriteriaDelete(BooksPDF.class);
        Root<BooksPDF> root = query.from(BooksPDF.class);
        query.where(builder.equal(root.get("fileName"), fileName));
        entityManager.createQuery(query).executeUpdate();
    }
}