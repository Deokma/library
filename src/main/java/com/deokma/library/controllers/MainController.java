package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.models.BooksCover;
import com.deokma.library.repo.BooksCoverRepository;
import com.deokma.library.repo.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private BooksRepository booksRepository;
    private BooksCoverRepository booksCoverRepository;
    private Authentication authentication;

    @Autowired
    public MainController(BooksRepository booksRepository, BooksCoverRepository booksCoverRepository) {
        this.booksRepository = booksRepository;
        this.booksCoverRepository = booksCoverRepository;
    }

    /**
     * Go to main page
     */
    @GetMapping("/")
    public String main(Model model) {
        Iterable<Books> books = booksRepository.findAll();
        Iterable<BooksCover> covers = booksCoverRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("cover", covers);

        //booksCoverRepository.createTempTable();

        return "home";
    }


}