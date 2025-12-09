package org.maks.expensosap.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SecurityHeadersConfig {

    @Bean
    public HttpFilter securityHeadersFilter() {
        return new HttpFilter() {
            @Override
            protected void doFilter(
                    HttpServletRequest req,
                    HttpServletResponse res,
                    FilterChain chain
            ) throws IOException, ServletException {

                // Basic security headers
                res.setHeader("X-Content-Type-Options", "nosniff");
                res.setHeader("X-Frame-Options", "DENY");
                res.setHeader("X-XSS-Protection", "1; mode=block");

                // Disable caching sensitive info
                res.setHeader("Cache-Control", "no-store");

                // Simple safe CSP
                res.setHeader("Content-Security-Policy",
                        "default-src 'self'; " +
                                "script-src 'self'; " +
                                "style-src 'self'; " +
                                "img-src 'self' data:;");

                super.doFilter(req, res, chain);
            }
        };
    }
}
