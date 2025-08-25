package com.kata.romannumbers.application;

import com.kata.romannumbers.application.exception.InvalidRomanNumberException;

import java.util.Map;
import java.util.regex.Pattern;

public class RomanToArabicNumberUseCase implements RomanToArabicNumberConverter {

    private static final Pattern VALID_ROMAN_PATTERN = Pattern.compile(
            "^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$"
    );

    private static final Map<Character, Integer> ROMAN_TO_ARABIC = Map.of(
            'I', 1,
            'V', 5,
            'X', 10,
            'L', 50,
            'C', 100,
            'D', 500,
            'M', 1000
    );

    @Override
    public Integer perform(String number) {
        validateRomanNumber(number);

        String normalizedRomanNumber = number.toUpperCase();
        int result = 0;
        int prevValue = 0;

        for (int i = normalizedRomanNumber.length() - 1; i >= 0; i--) {
            char currentChar = normalizedRomanNumber.charAt(i);
            Integer currentValue = ROMAN_TO_ARABIC.get(currentChar);

            if (currentValue == null) {
                throw new InvalidRomanNumberException(
                        String.format("Invalid character '%c' in roman number", currentChar));
            }

            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }

    private void validateRomanNumber(String romanNumber) {
        if (romanNumber == null || romanNumber.isEmpty()) {
            throw new InvalidRomanNumberException("Roman number cannot be null or empty");
        }

        if (!VALID_ROMAN_PATTERN.matcher(romanNumber.toUpperCase()).matches()) {
            throw new InvalidRomanNumberException(
                    String.format("Invalid roman number format: %s", romanNumber));
        }
    }
}
