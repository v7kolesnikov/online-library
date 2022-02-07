package com.home.onlinelibrary.controllers;

import com.home.onlinelibrary.controllers.params.RegistrationState;
import com.home.onlinelibrary.domain.User;
import com.home.onlinelibrary.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorisationController {
    private final UsersService userService;

    public static final String REGISTRATION_PAGE = "registration";

    public AuthorisationController(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        RegistrationState registrationState = new RegistrationState();
        model.addAttribute("registrationState", registrationState);
        return REGISTRATION_PAGE;
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        boolean isNewUser = userService.addUser(user);
        RegistrationState registrationState = new RegistrationState();

        if (!isNewUser) {
            registrationState.setRegistrationState(true);
            model.addAttribute("registrationState", registrationState);
            return REGISTRATION_PAGE;
        }

        return "redirect:/login";
    }
}
