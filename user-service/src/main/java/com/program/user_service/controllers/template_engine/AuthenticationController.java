package com.program.user_service.controllers.template_engine;

import com.program.user_service.entities.support_entities.response_enitites.AuthenticationResponse;
import com.program.user_service.entities.support_entities.request_entities.LoginRequest;
import com.program.user_service.entities.support_entities.request_entities.RegisterRequest;
import com.program.user_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    @NonNull private AuthenticationService authenticationService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {
        log.info("Register request received: {}", request);
        if (bindingResult.hasErrors()) {
            return "validation_errors";
        }
        AuthenticationResponse response = authenticationService.register(request);
        model.addAttribute("authResponse", response);
        return "registration";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest");
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
                        BindingResult bindingResult,
                        Model model) {
        AuthenticationResponse response = authenticationService.login(request);
        model.addAttribute("authResponse", response);
        return "login";
    }
}