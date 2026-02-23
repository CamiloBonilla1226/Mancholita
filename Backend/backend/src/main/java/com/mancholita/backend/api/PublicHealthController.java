package com.mancholita.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicHealthController {

    @GetMapping("/api/public/health")
    public String health() {
        return "OK";
    }
}