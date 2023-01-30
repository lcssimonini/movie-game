package com.ada.moviegame.exception;

public class GameEndedException extends RuntimeException {

  public GameEndedException(String errorMessage) {
    super(errorMessage);
  }
}
