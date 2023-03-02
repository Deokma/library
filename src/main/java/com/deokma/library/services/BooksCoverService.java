package com.deokma.library.services;

import com.deokma.library.exceptions.ResourceNotFoundException;
import com.deokma.library.models.BooksCover;
import com.deokma.library.models.BooksPDF;
import com.deokma.library.repo.BooksCoverRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private EntityManager entityManager;
    @Transactional
    public void deleteByFileName(String fileName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<BooksCover> query = builder.createCriteriaDelete(BooksCover.class);
        Root<BooksCover> root = query.from(BooksCover.class);
        query.where(builder.equal(root.get("fileName"), fileName));
        entityManager.createQuery(query).executeUpdate();
    }
}