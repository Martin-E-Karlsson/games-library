package org.example.gameslibrary.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "error";
    private static final String ERROR_MESSAGE = "errorMessage";

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute(ERROR_MESSAGE, ex.getMessage());
        return ERROR;
    }

    @ExceptionHandler(DuplicateTitleException.class)
    public String handleDuplicateTitle(DuplicateTitleException ex, Model model) {
        model.addAttribute(ERROR_MESSAGE, ex.getMessage());
        return ERROR;
    }

    @ExceptionHandler(InvalidPriceException.class)
    public String handleInvalidPrice(InvalidPriceException ex, Model model) {
        model.addAttribute(ERROR_MESSAGE, ex.getMessage());
        return ERROR;
    }
}