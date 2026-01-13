package com.example.dummy_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class SecretController {

    @GetMapping
    public String getSecret() {
        return "🎉 SUCCESS: You have accessed the protected data!";
    }
}