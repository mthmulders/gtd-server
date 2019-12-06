package com.infosupport.training.reactjs.gtdserver.users;

import com.infosupport.training.reactjs.gtdserver.security.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@Slf4j
public class SecuredUsersController {
    @GetMapping("/me")
    public User getCurrent(@AuthenticationPrincipal final User user) {
        return user;
    }

}
