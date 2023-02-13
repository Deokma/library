package com.deokma.library.repo;

import com.deokma.library.models.BooksPDF;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Denis Popolamov
 */
public interface BooksPdfRepository extends JpaRepository<BooksPDF, Long> {
}