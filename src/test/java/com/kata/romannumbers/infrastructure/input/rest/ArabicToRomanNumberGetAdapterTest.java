package com.kata.romannumbers.infrastructure.input.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Arabic to Roman Numbers Controller Adapter Test")
public class ArabicToRomanNumberGetAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String URL_BASE = "/api/v1/roman-numbers/arabic-to-roman";

    @Test
    @DisplayName("Should convert Arabic number to Roman successfully")
    void shouldConvertArabicToRoman() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("number", "42"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.arabic").value(42))
                .andExpect(jsonPath("$.roman").value("XLII"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, I",
            "4, IV",
            "5, V",
            "9, IX",
            "10, X",
            "50, L",
            "100, C",
            "500, D",
            "1000, M",
            "1994, MCMXCIV",
            "3999, MMMCMXCIX"
    })
    @DisplayName("Should convert various Arabic numbers correctly")
    void shouldConvertVariousArabicNumbers(int number, String expectedRoman) throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("number", String.valueOf(number)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arabic").value(number))
                .andExpect(jsonPath("$.roman").value(expectedRoman));
    }

    @Test
    @DisplayName("Should return 400 for number below minimum")
    void shouldReturn400ForNumberBelowMinimum() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("number", "0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Arabic Number"))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Number must be between 1 and 3999")));
    }

    @Test
    @DisplayName("Should return 400 for number above maximum")
    void shouldReturn400ForNumberAboveMaximum() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("number", "4000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Arabic Number"))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Number must be between 1 and 3999")));
    }

    @Test
    @DisplayName("Should return 400 for negative number")
    void shouldReturn400ForNegativeNumber() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("number", "-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Arabic Number"));
    }

    @Test
    @DisplayName("Should return 400 for invalid number format")
    void shouldReturn400ForInvalidNumberFormat() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("number", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Type mismatch error"))
                .andExpect(jsonPath("$.message")
                        .value(containsString("cannot be converted to Integer")));
    }

    @Test
    @DisplayName("Should return 400 when number parameter is missing")
    void shouldReturn400WhenNumberParameterMissing() throws Exception {
        mockMvc.perform(get(URL_BASE))
                .andExpect(status().isBadRequest());
    }
}
