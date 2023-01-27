package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Denis Popolamov
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserService userService;

    //@RequestMapping(value = "/admin", method = RequestMethod.GET)
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.list());
        return "admin";
    }

    //@RequestMapping(value = "/admin/user/ban/{id}", method = RequestMethod.POST)
    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    //@RequestMapping(value = "/admin/user/unban/{id}", method = RequestMethod.POST)
    @PostMapping("/admin/user/unban/{id}")
    public String userUnban(@PathVariable("id") Long id) {
        userService.unbanUser(id);
        return "redirect:/admin";
    }
}
