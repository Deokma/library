package com.deokma.library.repo;

import com.deokma.library.models.BooksPDF;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Denis Popolamov
 */
public interface BooksPdfRepository extends CrudRepository<BooksPDF, Long> {
    void deleteByFileName(String fileName);
}
