package com.deokma.library.controllers;

import com.deokma.library.models.User;
import com.deokma.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * @author Denis Popolamov
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Go to registration page
     */
    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    /**
     * Go to login page
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Method for create user
     *
     * @param user  User
     * @param model Model
     */
    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "User with Username " + user.getUsername() + " already exist.");
            return "registration";
        }
        userService.createUser(user);
        return "redirect:/login";
    }

    /**
     * Go to account page
     *
     * @param model     Model
     * @param principal The user who logged in
     */
    @GetMapping("/account")
    public String accountPage(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("book_list", user.getBooks_list());
        return "account";
    }

    /**
     * Go to another user page
     *
     * @param user      User
     * @param model     Model
     * @param principal The user who logged in
     */
    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        User user_principal = userService.getUserByPrincipal(principal);
        if (user_principal == user) {
            return "redirect:/account";
        }
        model.addAttribute("book_list", user.getBooks_list());
        return "user-info";
    }

}
