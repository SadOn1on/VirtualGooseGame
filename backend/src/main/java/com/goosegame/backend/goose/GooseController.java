package com.goosegame.backend.goose;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/goose")
public class GooseController {

    private final GooseService service;

    @Autowired
    public GooseController(GooseService service) {
        this.service = service;
    }

    @GetMapping
    public Goose getGoose(Principal principal) {
        return service.findByUsername(principal.getName());
    }
}
