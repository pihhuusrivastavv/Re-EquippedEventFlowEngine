package com.anshika.Re_EquippedEventFlowEngine.Exception;

import com.anshika.Re_EquippedEventFlowEngine.Web.Response.EventErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerGlobal
{
    @ExceptionHandler(EventErrorException.class)
    public EventErrorResponse handleEventErrorException(EventErrorException e)
    {
        return new EventErrorResponse(e.getMessage(),HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }
}
