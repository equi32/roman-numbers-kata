package com.kata.romannumbers.infrastructure.input.rest.exception;

import com.kata.romannumbers.application.exception.InvalidArabicNumberException;
import com.kata.romannumbers.application.exception.InvalidRomanNumberException;
import com.kata.romannumbers.infrastructure.input.rest.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRomanNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRomanNumberException(InvalidRomanNumberException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid Roman Number", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidArabicNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArabicNumberException(InvalidArabicNumberException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid Arabic Number", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid parameter type: '%s' cannot be converted to %s",
                ex.getValue(), ex.getRequiredType().getSimpleName());
        ErrorResponse errorResponse = new ErrorResponse(
                "Type mismatch error",
                message
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Missing Request Parameter",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal server error",
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
