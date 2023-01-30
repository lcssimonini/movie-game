package com.ada.moviegame.exception;

public class PasswordConfirmationException extends RuntimeException {

  public PasswordConfirmationException(String errorMessage) {
    super(errorMessage);
  }
}
