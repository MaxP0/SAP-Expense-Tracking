package org.maks.expensosap.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.maks.expensosap.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LoggingService loggingService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {

        String username = body.get("username");
        String password = body.get("password");

        loggingService.log(
                "login_insecure_raw",
                null,
                "username=" + username + ", password=" + password
        );

        String sql = "SELECT id, username, password, role FROM users WHERE username = '"
                + username + "' AND password = '" + password + "'";

        List<?> result = em.createNativeQuery(sql).getResultList();

        if (result.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error","invalid"));
        }

        return ResponseEntity.ok(result.get(0)); // raw DB row
    }
}