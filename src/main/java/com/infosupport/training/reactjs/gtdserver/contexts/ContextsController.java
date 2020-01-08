package com.infosupport.training.reactjs.gtdserver.contexts;

import com.infosupport.training.reactjs.gtdserver.security.User;
import com.infosupport.training.reactjs.gtdserver.tasks.Task;
import com.infosupport.training.reactjs.gtdserver.tasks.TaskService;
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
    private final ContextsService contextsService;
    private final TaskService taskService;

    @GetMapping
    public Collection<Context> getAllContexts(@AuthenticationPrincipal final User user) {
        return contextsService.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<Context> createContext(@AuthenticationPrincipal final User user,
                                                 @RequestBody final Context context) {
        final Context stored = contextsService.save(user, context.withUserId(user.getId()));
        return ResponseEntity.status(CREATED).body(stored);
    }

    @RequestMapping("/{contextId}/tasks")
    @GetMapping
    public ResponseEntity<Collection<Task>> getAllTasksForContext(@AuthenticationPrincipal final User user,
                                                                  @PathVariable("contextId") final UUID contextId) {
        return contextsService.findByUserIdAndId(user.getId(), contextId)
                .map((context) -> taskService.findTasksByUserAndContext(user.getId(), contextId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Context> updateContext(@AuthenticationPrincipal final User user,
                                                 @PathVariable("id") final UUID id,
                                                 @RequestBody final Context context) {
        return contextsService.save(user, id, context)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
