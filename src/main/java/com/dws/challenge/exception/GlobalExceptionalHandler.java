package com.dws.challenge.exception;

import com.dws.challenge.response.ErrorResponse;
import com.dws.challenge.response.MoneyTransferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountException(RuntimeException exception) {
        return new ErrorResponse(MoneyTransferResponse.Status.FAILURE.name(), exception.getMessage());
    }

    @ExceptionHandler(value = InSufficientAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAmountException(RuntimeException exception) {
        return new ErrorResponse(MoneyTransferResponse.Status.FAILURE.name(), exception.getMessage());
    }
}
