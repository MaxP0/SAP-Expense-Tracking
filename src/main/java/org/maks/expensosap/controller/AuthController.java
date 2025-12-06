package org.maks.expensosap.controller;

import org.maks.expensosap.model.User;
import org.maks.expensosap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User creds) {
        User user = userRepository.findByUsernameAndPassword(creds.getUsername(), creds.getPassword());
        if (user == null) return ResponseEntity.status(401).body("Invalid credentials");
        // Not secure yet — just return the user object, no session. Add JWT/session later.
        return ResponseEntity.ok(user);
    }
}
