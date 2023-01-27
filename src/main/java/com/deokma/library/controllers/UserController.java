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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    //@RequestMapping(value = "/registration", method = RequestMethod.GET)
    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    //@RequestMapping(value = "/login", method = RequestMethod.GET)
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    //@RequestMapping(value = "/registration", method = RequestMethod.POST)
    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с username " + user.getUsername() + " уже существует");
            return "registration";
        }
        userService.createUser(user);
        return "redirect:/login";
    }

    //@RequestMapping(value = "/account", method = RequestMethod.GET)
    @GetMapping("/account")
    public String accountPage(Model model, Principal principal) {
        // model.addAttribute("user", user);
        //Iterable<Books> books = booksRepository.findAll();
        User user = userService.getUserByPrincipal(principal);

        //Books user_books = booksRepository.findById(user.getBooks_list());
        model.addAttribute("book_list",user.getBooks_list());
        //model.addAttribute("books", books);
       // model.addAttribute("userok");

        return "account";
    }

    //@RequestMapping(value = "/user/{user}", method = RequestMethod.GET)
    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model) {
        // model.addAttribute("user", user);
        //Iterable<Books> books = booksRepository.findAll();
        Optional<User> user1 = userRepository.findById(user.getId());
        model.addAttribute("book_list",user.getBooks_list());
        //model.addAttribute("books", books);
        return "user-info";
    }

}
