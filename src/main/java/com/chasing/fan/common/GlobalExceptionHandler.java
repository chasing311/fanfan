package com.chasing.fan.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        String message = exception.getMessage();
        log.error(message);
        if (message.contains("Duplicate entry")) {
            String key = message.split(" ")[2];
            return Result.error(key + "已存在");
        }
        return Result.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException exception) {
        log.error(exception.getMessage());
        return Result.error(exception.getMessage());
    }
}
