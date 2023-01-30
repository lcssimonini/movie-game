package com.ada.moviegame.exception;

public class WrongUserException extends RuntimeException {

  public WrongUserException(String errorMessage) {
    super(errorMessage);
  }
}
