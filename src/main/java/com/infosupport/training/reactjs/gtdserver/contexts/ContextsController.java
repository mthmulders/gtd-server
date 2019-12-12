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
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("/contexts")
public class ContextsController {
    private final ContextsService service;

    @GetMapping
    public Collection<Context> getAllContexts(@AuthenticationPrincipal final User user) {
        return service.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<Context> createContext(@AuthenticationPrincipal final User user,
                                                 @RequestBody final Context context) {
        final Context stored = service.save(user, context.withUserId(user.getId()));
        return ResponseEntity.status(CREATED).body(stored);
    }

    @PostMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Context> updateContext(@AuthenticationPrincipal final User user,
                                                 @PathVariable("id") final UUID id,
                                                 @RequestBody final Context context) {
        return service.save(user, id, context)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
