package com.polsl.bank.domain.advice;

import com.polsl.bank.exceptions.ExpiredOrFraudulentBill;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ErrorHandlers {

    @ExceptionHandler(ExpiredOrFraudulentBill.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorResponse handleMissingApiKeyHeaderException(HttpServletRequest req, ExpiredOrFraudulentBill ex) {
        return ApiErrorResponse.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
    }

}