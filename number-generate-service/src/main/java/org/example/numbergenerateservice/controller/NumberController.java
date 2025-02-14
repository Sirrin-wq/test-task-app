package org.example.numbergenerateservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.numbergenerateservice.model.GeneratedNumberEntity;
import org.example.numbergenerateservice.service.NumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/numbers")
@RequiredArgsConstructor
public class NumberController {
    private final NumberService service;

    @GetMapping
    public ResponseEntity<String> generateNumber() {
        GeneratedNumberEntity number = service.generate();
        return ResponseEntity.ok(number.getFullNumber());
    }
}
