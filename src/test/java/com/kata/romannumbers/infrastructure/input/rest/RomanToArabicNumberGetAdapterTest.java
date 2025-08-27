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
@DisplayName("Roman to Arabic Numbers Controller Adapter Test")
public class RomanToArabicNumberGetAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String URL_BASE = "/api/v1/roman-numbers/roman-to-arabic";

    @Test
    @DisplayName("Should convert Roman number to Arabic successfully")
    void shouldConvertRomanToArabic() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", "XLII"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roman").value("XLII"))
                .andExpect(jsonPath("$.arabic").value(42));
    }

    @ParameterizedTest
    @CsvSource({
            "I, 1",
            "IV, 4",
            "V, 5",
            "IX, 9",
            "X, 10",
            "L, 50",
            "C, 100",
            "D, 500",
            "M, 1000",
            "MCMXCIV, 1994",
            "MMMCMXCIX, 3999"
    })
    @DisplayName("Should convert various Roman numbers correctly")
    void shouldConvertVariousRomannumbers(String roman, int expectedArabic) throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", roman))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roman").value(roman))
                .andExpect(jsonPath("$.arabic").value(expectedArabic));
    }

    @Test
    @DisplayName("Should handle lowercase Roman numbers")
    void shouldHandleLowercaseRomannumbers() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", "xlii"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roman").value("XLII"))
                .andExpect(jsonPath("$.arabic").value(42));
    }

    @Test
    @DisplayName("Should handle mixed case Roman numbers")
    void shouldHandleMixedCaseRomannumbers() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", "XlIi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roman").value("XLII"))
                .andExpect(jsonPath("$.arabic").value(42));
    }

    @Test
    @DisplayName("Should return 400 for Invalid Roman Number")
    void shouldReturn400ForInvalidRomannumber() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", "IIII"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Roman Number"))
                .andExpect(jsonPath("$.message").value(containsString("Invalid roman number format")));
    }

    @Test
    @DisplayName("Should return 400 for Roman number with invalid characters")
    void shouldReturn400ForRomanWithInvalidCharacters() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", "ABC"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Roman Number"));
    }

    @Test
    @DisplayName("Should return 400 for empty Roman number")
    void shouldReturn400ForEmptyRomannumber() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .param("roman", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Roman Number"))
                .andExpect(jsonPath("$.message").value(containsString("cannot be null or empty")));
    }

    @Test
    @DisplayName("Should return 400 when Roman parameter is missing")
    void shouldReturn400WhenRomanParameterMissing() throws Exception {
        mockMvc.perform(get(URL_BASE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for Invalid Roman Number patterns")
    void shouldReturn400ForInvalidPatterns() throws Exception {
        // Test various invalid patterns
        String[] invalidPatterns = {"VV", "LL", "DD", "IC", "IM", "XD", "XM", "VX", "IL", "IIIII"};

        for (String pattern : invalidPatterns) {
            mockMvc.perform(get(URL_BASE)
                            .param("roman", pattern))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Invalid Roman Number"));
        }
    }
}
