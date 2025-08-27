package com.kata.romannumbers.infrastructure.input.rest;

import com.kata.romannumbers.application.ArabicToRomanNumberConverter;
import com.kata.romannumbers.infrastructure.input.rest.model.ArabicToRomanNumberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/roman-numbers/arabic-to-roman")
@RequiredArgsConstructor
public class ArabicToRomanNumberGetAdapter {

    private final ArabicToRomanNumberConverter converter;

    @GetMapping
    public ResponseEntity<ArabicToRomanNumberResponse> perform(@RequestParam Integer number) {
        log.info("GET /api/v1/roman-numbers/arabic-to-roman with arabic number {}", number);
        return ResponseEntity.ok(buildResponse(number, converter.perform(number)));
    }

    private ArabicToRomanNumberResponse buildResponse(Integer arabic, String roman) {
        return new ArabicToRomanNumberResponse(arabic, roman);
    }
}
