package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.repo.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private BooksRepository booksRepository;

    @Autowired
    public MainController(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    /**
     * Go to main page
     */
    @GetMapping("/")
    public String main(Model model) {
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        return "home";
    }



}