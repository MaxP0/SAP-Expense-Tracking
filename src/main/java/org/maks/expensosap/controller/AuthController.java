package org.maks.expensosap.controller;

import lombok.RequiredArgsConstructor;
import org.maks.expensosap.model.User;
import org.maks.expensosap.repository.UserRepository;
import org.maks.expensosap.service.LoggingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final LoggingService loggingService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username exists");
        }

        // no raw password in logs
        loggingService.log(
                "register",
                null,
                "username=" + user.getUsername()
        );

        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);

        userRepository.save(user);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User creds) {

        loggingService.log(
                "login_attempt",
                null,
                "username=" + creds.getUsername()
        );

        User user = userRepository.findByUsername(creds.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        if (!passwordEncoder.matches(creds.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // small step towards secure: now it doesnt return password
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }
}
