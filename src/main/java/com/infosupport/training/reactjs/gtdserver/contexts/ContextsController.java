package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/contexts")
public class ContextsController {
    private final ContextRepository contextRepository;

    @GetMapping
    public Collection<Context> getAllContexts(@AuthenticationPrincipal final User user) {
        return contextRepository.findByUserId(user.getId());
    }

    @PostMapping
    public void createContext(@AuthenticationPrincipal final User user,
                              @RequestBody final Context context)
    {
        contextRepository.save(context.withUserId(user.getId()));
    }
}
