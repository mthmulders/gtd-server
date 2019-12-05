package com.infosupport.training.reactjs.gtdserver.contexts;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ContextsController {
    private final ContextRepository contextRepository;

    @RequestMapping(value = "/contexts", method = GET)
    public Collection<Context> getAllContexts() {
        return contextRepository.getAllContexts();
    }
}
