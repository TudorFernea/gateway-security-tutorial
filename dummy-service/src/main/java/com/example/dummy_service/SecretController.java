package com.example.dummy_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class SecretController {

    @GetMapping
    public String getSecret(@RequestHeader(value = "X-USER-ROLE", defaultValue = "UNKNOWN") String role) {
        if ("ADMIN".equals(role)) {
            return "WELCOME ADMIN! Here is the SECRET data.";
        } else {
            return "Hello Guest. Here is the public data.";
        }
    }
}