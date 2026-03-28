package com.deokma.library.services;

import com.deokma.library.models.Books;

import java.util.List;
import java.util.Map;

public interface BooksService {
    List<Books> findParetoOptimalBooks(Map<String, Object> criteria);
}
