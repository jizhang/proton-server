package com.shzhangji.proton.controller;

import com.shzhangji.proton.AppException;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(AppException.class)
  public AppExceptionResponse handleAppException(AppException e) {
    return new AppExceptionResponse(e.getMessage(), e.getCode());
  }

  public record AppExceptionResponse(String message, int code) { }
}
