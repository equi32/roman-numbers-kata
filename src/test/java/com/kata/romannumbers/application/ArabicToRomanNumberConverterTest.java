package com.kata.romannumbers.application;

import com.kata.romannumbers.application.exception.InvalidArabicNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Arabic to Roman Number Converter Test")
public class ArabicToRomanNumberConverterTest {

    private ArabicToRomanNumberConverterUseCase converter;

    @BeforeEach
    void setUp() {
        converter = new ArabicToRomanNumberConverterUseCase();
    }

    @Nested
    @DisplayName("Arabic to Roman Conversion")
    class ArabicToRomanTests {

        @Test
        @DisplayName("Should convert basic single digit numbers")
        void shouldConvertBasicNumbers() {
            assertThat(converter.perform(1)).isEqualTo("I");
            assertThat(converter.perform(5)).isEqualTo("V");
            assertThat(converter.perform(10)).isEqualTo("X");
            assertThat(converter.perform(50)).isEqualTo("L");
            assertThat(converter.perform(100)).isEqualTo("C");
            assertThat(converter.perform(500)).isEqualTo("D");
            assertThat(converter.perform(1000)).isEqualTo("M");
        }

        @ParameterizedTest
        @CsvSource({
                "2, II",
                "3, III",
                "4, IV",
                "9, IX",
                "21, XXI",
                "44, XLIV",
                "49, XLIX",
                "90, XC",
                "99, XCIX",
                "400, CD",
                "900, CM",
                "1994, MCMXCIV",
                "2024, MMXXIV",
                "2025, MMXXV"
        })
        @DisplayName("Should convert various numbers correctly")
        void shouldConvertVariousNumbers(int arabic, String expectedRoman) {
            assertThat(converter.perform(arabic)).isEqualTo(expectedRoman);
        }

        @Test
        @DisplayName("Border Case: Should convert minimum valid value (1)")
        void shouldConvertMinimumValue() {
            assertThat(converter.perform(1)).isEqualTo("I");
        }

        @Test
        @DisplayName("Border Case: Should convert maximum valid value (3999)")
        void shouldConvertMaximumValue() {
            assertThat(converter.perform(3999)).isEqualTo("MMMCMXCIX");
        }

        @Test
        @DisplayName("Border Case: Should throw exception for zero")
        void shouldThrowExceptionForZero() {
            assertThatThrownBy(() -> converter.perform(0))
                    .isInstanceOf(InvalidArabicNumberException.class)
                    .hasMessageContaining("Number must be between 1 and 3999");
        }

        @Test
        @DisplayName("Border Case: Should throw exception for negative numbers")
        void shouldThrowExceptionForNegativeNumbers() {
            assertThatThrownBy(() -> converter.perform(-1))
                    .isInstanceOf(InvalidArabicNumberException.class)
                    .hasMessageContaining("Number must be between 1 and 3999");

            assertThatThrownBy(() -> converter.perform(-100))
                    .isInstanceOf(InvalidArabicNumberException.class);
        }

        @Test
        @DisplayName("Border Case: Should throw exception for numbers greater than 3999")
        void shouldThrowExceptionForNumbersGreaterThan3999() {
            assertThatThrownBy(() -> converter.perform(4000))
                    .isInstanceOf(InvalidArabicNumberException.class)
                    .hasMessageContaining("Number must be between 1 and 3999");

            assertThatThrownBy(() -> converter.perform(5000))
                    .isInstanceOf(InvalidArabicNumberException.class);
        }

        @Test
        @DisplayName("Should handle subtractive notation correctly")
        void shouldHandleSubtractiveNotation() {
            assertThat(converter.perform(4)).isEqualTo("IV");
            assertThat(converter.perform(9)).isEqualTo("IX");
            assertThat(converter.perform(40)).isEqualTo("XL");
            assertThat(converter.perform(90)).isEqualTo("XC");
            assertThat(converter.perform(400)).isEqualTo("CD");
            assertThat(converter.perform(900)).isEqualTo("CM");
            assertThat(converter.perform(444)).isEqualTo("CDXLIV");
            assertThat(converter.perform(949)).isEqualTo("CMXLIX");
        }
    }
}
