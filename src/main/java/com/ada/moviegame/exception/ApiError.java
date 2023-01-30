package com.ada.moviegame.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Value
@Builder
public class ApiError {
  String title;
  Integer status;
  Map<String, List<String>> errors;

  public static ApiError notFound(NotFoundException e) {
    return ApiError.builder().title(e.getMessage()).status(HttpStatus.NOT_FOUND.value()).build();
  }

  public static ApiError serverError(Exception e) {
    return ApiError.builder()
        .title(e.getMessage())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .build();
  }

  public static ApiError validationError(BindingResult bindingResult) {
    return validationError(getErrors(bindingResult));
  }

  public static ApiError validationError(Map<String, List<String>> errors) {
    return ApiError.builder()
        .title("One or more validation errors occurred.")
        .status(HttpStatus.BAD_REQUEST.value())
        .errors(errors)
        .build();
  }

  private static Map<String, List<String>> getErrors(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .collect(
            Collectors.groupingBy(
                v -> "$." + v.getField(),
                Collectors.mapping(
                    DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));
  }

  public static ApiError gameEnded(GameEndedException e) {
    return ApiError.builder().title(e.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build();
  }

  public static ApiError wrongPasswordConfirmation(PasswordConfirmationException e) {
    return ApiError.builder().title(e.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build();
  }

  public static ApiError wrongUser(WrongUserException e) {
    return ApiError.builder().title(e.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build();
  }

  public static ApiError handleValidationException(MethodArgumentNotValidException e) {
    return validationError(e.getBindingResult());
  }
}
