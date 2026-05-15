package com.stockmanagement.hotelmanagement.common;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidRequestException.class)
    public String handleInvalidRequest(InvalidRequestException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("error", "Page not found");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", "An error occurred: " + ex.getMessage());
        ex.printStackTrace();
        return "error";
    }
}




