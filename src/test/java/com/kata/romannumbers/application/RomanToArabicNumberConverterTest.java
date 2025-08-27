package com.kata.romannumbers.application;


import com.kata.romannumbers.application.exception.InvalidRomanNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Roman to Arabic Number Converter Test")
public class RomanToArabicNumberConverterTest {

    private RomanToArabicNumberConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RomanToArabicNumberConverterUseCase();
    }

    @Nested
    @DisplayName("Roman to Arabic Conversion")
    class RomanToArabicTests {

        @Test
        @DisplayName("Should convert basic roman numbers")
        void shouldConvertBasicRomanNumbers() {
            assertThat(converter.perform("I")).isEqualTo(1);
            assertThat(converter.perform("V")).isEqualTo(5);
            assertThat(converter.perform("X")).isEqualTo(10);
            assertThat(converter.perform("L")).isEqualTo(50);
            assertThat(converter.perform("C")).isEqualTo(100);
            assertThat(converter.perform("D")).isEqualTo(500);
            assertThat(converter.perform("M")).isEqualTo(1000);
        }

        @ParameterizedTest
        @CsvSource({
                "II, 2",
                "III, 3",
                "IV, 4",
                "IX, 9",
                "XXI, 21",
                "XLIV, 44",
                "XLIX, 49",
                "XC, 90",
                "XCIX, 99",
                "CD, 400",
                "CM, 900",
                "MCMXCIV, 1994",
                "MMXXIV, 2024"
        })
        @DisplayName("Should convert various roman numbers correctly")
        void shouldConvertVariousRomanNumbers(String roman, int expectedArabic) {
            assertThat(converter.perform(roman)).isEqualTo(expectedArabic);
        }

        @Test
        @DisplayName("Should handle lowercase roman numbers")
        void shouldHandleLowercaseRomanNumbers() {
            assertThat(converter.perform("iv")).isEqualTo(4);
            assertThat(converter.perform("xxi")).isEqualTo(21);
            assertThat(converter.perform("mcmxciv")).isEqualTo(1994);
        }

        @Test
        @DisplayName("Should handle mixed case roman numbers")
        void shouldHandleMixedCaseRomanNumbers() {
            assertThat(converter.perform("Iv")).isEqualTo(4);
            assertThat(converter.perform("XxI")).isEqualTo(21);
            assertThat(converter.perform("McmXCIV")).isEqualTo(1994);
        }

        @Test
        @DisplayName("Border Case: Should convert minimum roman number (I)")
        void shouldConvertMinimumRomanNumber() {
            assertThat(converter.perform("I")).isEqualTo(1);
        }

        @Test
        @DisplayName("Border Case: Should convert maximum roman number (MMMCMXCIX)")
        void shouldConvertMaximumRomanNumber() {
            assertThat(converter.perform("MMMCMXCIX")).isEqualTo(3999);
        }

        @Test
        @DisplayName("Border Case: Should throw exception for null input")
        void shouldThrowExceptionForNullInput() {
            assertThatThrownBy(() -> converter.perform(null))
                    .isInstanceOf(InvalidRomanNumberException.class)
                    .hasMessageContaining("Roman number cannot be null or empty");
        }

        @Test
        @DisplayName("Border Case: Should throw exception for empty string")
        void shouldThrowExceptionForEmptyString() {
            assertThatThrownBy(() -> converter.perform(""))
                    .isInstanceOf(InvalidRomanNumberException.class)
                    .hasMessageContaining("Roman number cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception for invalid characters")
        void shouldThrowExceptionForInvalidCharacters() {
            assertThatThrownBy(() -> converter.perform("ABC"))
                    .isInstanceOf(InvalidRomanNumberException.class)
                    .hasMessageContaining("Invalid roman number format");

            assertThatThrownBy(() -> converter.perform("X1V"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            assertThatThrownBy(() -> converter.perform("I V"))
                    .isInstanceOf(InvalidRomanNumberException.class);
        }

        @Test
        @DisplayName("Should throw exception for invalid roman number format")
        void shouldThrowExceptionForInvalidFormat() {
            // More than 3 consecutive same characters (except M)
            assertThatThrownBy(() -> converter.perform("IIII"))
                    .isInstanceOf(InvalidRomanNumberException.class)
                    .hasMessageContaining("Invalid roman number format");

            assertThatThrownBy(() -> converter.perform("VV"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            assertThatThrownBy(() -> converter.perform("LL"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            assertThatThrownBy(() -> converter.perform("DD"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            // Invalid subtractive combinations
            assertThatThrownBy(() -> converter.perform("IC"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            assertThatThrownBy(() -> converter.perform("IM"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            assertThatThrownBy(() -> converter.perform("XD"))
                    .isInstanceOf(InvalidRomanNumberException.class);

            assertThatThrownBy(() -> converter.perform("VX"))
                    .isInstanceOf(InvalidRomanNumberException.class);
        }

        @Test
        @DisplayName("Should handle edge cases with subtractive notation")
        void shouldHandleSubtractiveNotationEdgeCases() {
            assertThat(converter.perform("IV")).isEqualTo(4);
            assertThat(converter.perform("IX")).isEqualTo(9);
            assertThat(converter.perform("XL")).isEqualTo(40);
            assertThat(converter.perform("XC")).isEqualTo(90);
            assertThat(converter.perform("CD")).isEqualTo(400);
            assertThat(converter.perform("CM")).isEqualTo(900);

            // Complex combinations
            assertThat(converter.perform("CDXLIV")).isEqualTo(444);
            assertThat(converter.perform("CMXLIX")).isEqualTo(949);
            assertThat(converter.perform("MCMXC")).isEqualTo(1990);
        }

        @ParameterizedTest
        @ValueSource(strings = {"MMMM", "MMMMM"})
        @DisplayName("Should accept valid sequences of M (up to 3999)")
        void shouldAcceptValidMSequences(String roman) {
            // MMMM would be 4000 which is out of range
            assertThatThrownBy(() -> converter.perform(roman))
                    .isInstanceOf(InvalidRomanNumberException.class);
        }
    }

//    @Nested
//    @DisplayName("Round-trip Conversion Tests")
//    class RoundTripTests {
//
//        @ParameterizedTest
//        @ValueSource(ints = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000, 1994, 2024, 3999})
//        @DisplayName("Should maintain consistency in round-trip conversion")
//        void shouldMaintainRoundTripConsistency(int number) {
//            String roman = converter.toRoman(number);
//            int result = converter.toArabic(roman);
//            assertThat(result).isEqualTo(number);
//        }
//
//        @Test
//        @DisplayName("Should handle all numbers from 1 to 100")
//        void shouldHandleFirst100Numbers() {
//            for (int i = 1; i <= 100; i++) {
//                String roman = converter.toRoman(i);
//                int result = converter.toArabic(roman);
//                assertThat(result)
//                        .as("Round-trip conversion failed for number %d", i)
//                        .isEqualTo(i);
//            }
//        }
//    }
}
