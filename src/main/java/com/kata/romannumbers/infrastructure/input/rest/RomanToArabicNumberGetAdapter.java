package com.kata.romannumbers.infrastructure.input.rest;

import com.kata.romannumbers.application.RomanToArabicNumberConverter;
import com.kata.romannumbers.infrastructure.input.rest.model.RomanToArabicNumberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/roman-numbers/roman-to-arabic")
@RequiredArgsConstructor
public class RomanToArabicNumberGetAdapter {

    private final RomanToArabicNumberConverter converter;

    @GetMapping
    public ResponseEntity<RomanToArabicNumberResponse> perform(@RequestParam String roman) {
        log.info("GET /api/v1/roman-numbers/roman-to-arabic with roman number {}", roman);
        return ResponseEntity.ok(buildResponse(roman, converter.perform(roman)));
    }

    private RomanToArabicNumberResponse buildResponse(String roman, Integer arabic) {
        return new RomanToArabicNumberResponse(roman.toUpperCase(), arabic);
    }
}
