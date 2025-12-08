package org.maks.expensosap.controller;

import lombok.RequiredArgsConstructor;
import org.maks.expensosap.model.User;
import org.maks.expensosap.repository.UserRepository;
import org.maks.expensosap.service.LoggingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final LoggingService loggingService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        loggingService.log(
                "register_raw",
                null,
                "username=" + user.getUsername() + ", password=" + user.getPassword()
        );
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User creds) {
        loggingService.log(
                "login_raw",
                null,
                "username=" + creds.getUsername() + ", password=" + creds.getPassword()
        );
        User user = userRepository.findByUsernameAndPassword(creds.getUsername(), creds.getPassword());
        if (user == null) return ResponseEntity.status(401).body("Invalid credentials");
        // Not secure yet — just return the user object, no session. Add JWT/session later.
        return ResponseEntity.ok(user);
    }
}
