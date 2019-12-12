package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<Context> createContext(@AuthenticationPrincipal final User user,
                                                 @RequestBody final Context context) {
        final Context stored = contextRepository.save(context.withUserId(user.getId()));
        return ResponseEntity.status(CREATED).body(stored);
    }

    @PostMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Context> updateContext(@AuthenticationPrincipal final User user,
                                                 @PathVariable("id") final UUID id,
                                                 @RequestBody final Context context) {
        // TODO Maarten this logic should go in a service, just like we do for Tasks

        // Verify the context actually exists for the current user.
        final Optional<Context> existing = contextRepository.findByUserIdAndId(user.getId(), id);

        if (!existing.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Make sure that identifying fields aren't changed.
        final Context updatedContext = context.
                withUserId(user.getId()).
                withId(existing.get().getId());

        final Context stored = contextRepository.save(updatedContext);
        return ResponseEntity.status(OK).body(stored);
    }
}
