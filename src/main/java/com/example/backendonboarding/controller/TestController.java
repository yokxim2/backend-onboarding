package com.example.backendonboarding.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured-endpoint")
public class TestController {

    @GetMapping
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Hello world");
    }
}
