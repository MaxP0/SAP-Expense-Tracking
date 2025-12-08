package org.maks.expensosap.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug")
public class DebugController {

    // reflected XSS - echoes param into response body without sanitization
    @GetMapping("/echo")
    public String echo(@RequestParam(defaultValue = "") String text) {
        // vulnerable: returns raw text that the browser may render as HTML
        return "<html><body>Echo: " + text + "</body></html>";
    }
}
