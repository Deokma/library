package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.repo.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Denis Popolamov
 */
@Controller
public class BooksController {

    @Autowired
    private BooksRepository booksRepository;
    @RequestMapping("/books")
    public String booksMain(Model model) {
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books",books);
        return "books-main";
    }
}
