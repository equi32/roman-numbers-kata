package com.kata.romannumbers.application;

import com.kata.romannumbers.application.exception.InvalidArabicNumberException;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ArabicToRomanNumberConverterUseCase implements ArabicToRomanNumberConverter {

    private static final Integer MIN_INT_VALUE = 1;
    private static final Integer MAX_INT_VALUE = 3999;
    private static final Map<Integer, String> ARABIC_TO_ROMAN = new LinkedHashMap<>();

    static {
        ARABIC_TO_ROMAN.put(1000, "M");
        ARABIC_TO_ROMAN.put(900, "CM");
        ARABIC_TO_ROMAN.put(500, "D");
        ARABIC_TO_ROMAN.put(400, "CD");
        ARABIC_TO_ROMAN.put(100, "C");
        ARABIC_TO_ROMAN.put(90, "XC");
        ARABIC_TO_ROMAN.put(50, "L");
        ARABIC_TO_ROMAN.put(40, "XL");
        ARABIC_TO_ROMAN.put(10, "X");
        ARABIC_TO_ROMAN.put(9, "IX");
        ARABIC_TO_ROMAN.put(5, "V");
        ARABIC_TO_ROMAN.put(4, "IV");
        ARABIC_TO_ROMAN.put(1, "I");
    }

    @Override
    public String perform(Integer number) {
        validateArabicNumber(number);
        StringBuilder result = new StringBuilder();
        Integer remaining = number;

        for(Map.Entry<Integer, String> entry : ARABIC_TO_ROMAN.entrySet()) {
            Integer value = entry.getKey();
            String romanNumber = entry.getValue();

            while (remaining >= value) {
                result.append(romanNumber);
                remaining -= value;
            }
        }

        return result.toString();
    }

    private void validateArabicNumber(Integer number) {
        if (number < MIN_INT_VALUE || number > MAX_INT_VALUE) {
            log.info("The number {} is out of range", number);
            throw new InvalidArabicNumberException(
                    String.format("Number must be between %d and %d, but was %d",
                            MIN_INT_VALUE, MAX_INT_VALUE, number));
        }
    }
}
