package com.infosupport.training.reactjs.gtdserver.contexts;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ContextsController {
    private final ContextRepository contextRepository;

    @GetMapping(value = "/contexts")
    public Collection<Context> getAllContexts() {
        return contextRepository.getAllContexts();
    }
}
