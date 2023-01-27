package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    @Autowired
    private BooksRepository booksRepository;

    //@RequestMapping(value = "/", method = RequestMethod.GET)
    @GetMapping("/")
    public String main(Model model, Principal principal) {
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        // model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "home";
    }


}