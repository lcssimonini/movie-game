package com.ada.moviegame.configuration;

import com.ada.moviegame.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingConfig {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ApiError onNotFoundException(NotFoundException exception) {
    return ApiError.notFound(exception);
  }

  @ExceptionHandler(GameEndedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiError onGameEnded(GameEndedException exception) {
    return ApiError.gameEnded(exception);
  }

  @ExceptionHandler(PasswordConfirmationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiError wrongPasswordConfirmation(PasswordConfirmationException exception) {
    return ApiError.wrongPasswordConfirmation(exception);
  }

  @ExceptionHandler(WrongUserException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public ApiError wrongUser(WrongUserException exception) {
    return ApiError.wrongUser(exception);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiError handleValidationException(MethodArgumentNotValidException exception) {
    return ApiError.handleValidationException(exception);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ApiError onException(Exception exception) {
    log.error("unhandled exception: {}", exception.getMessage(), exception);
    return ApiError.serverError(exception);
  }
}
