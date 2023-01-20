package com.deokma.library.controllers;

import com.deokma.library.models.User;
import com.deokma.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {
private final UserService userService;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model, Principal principal) {
       // model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "home";
    }


}