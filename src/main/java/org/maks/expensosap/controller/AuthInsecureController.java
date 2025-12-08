package org.maks.expensosap.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthInsecureController {

    @PersistenceContext
    private EntityManager em;

    // intentionally vulnerable login - raw query concatenation
    @PostMapping("/login-insecure")
    public ResponseEntity<?> loginInsecure(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // vulnerable: string concatenation -> SQLi
        String sql = "SELECT id, username, password, role FROM users WHERE username = '"
                + username + "' AND password = '" + password + "'";
        List<?> result = em.createNativeQuery(sql).getResultList();

        if (result.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error","invalid"));
        }

        // return first row raw — includes password (sensitive)
        Object row = result.get(0);
        return ResponseEntity.ok(row);
    }
}

