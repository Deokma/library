package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.repo.UserRepository;
import com.deokma.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

/**
 * @author Denis Popolamov
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    private BooksRepository booksRepository;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationPage() {
        return "registration";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с username " + user.getUsername() + " уже существует");
            return "registration";
        }
        userService.createUser(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String accountPage(Model model) {
        // model.addAttribute("user", user);
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        return "account";
    }

    @RequestMapping(value = "/user/{user}", method = RequestMethod.GET)
    public String userInfo(@PathVariable("user") User user, Model model) {
        // model.addAttribute("user", user);
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        return "user-info";
    }

}
