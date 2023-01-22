package com.ada.moviegame.security.service;

import static com.ada.moviegame.security.domain.Role.USER;

import com.ada.moviegame.exception.PasswordConfirmationException;
import com.ada.moviegame.security.controller.dto.AuthenticationRequest;
import com.ada.moviegame.security.controller.dto.AuthenticationResponse;
import com.ada.moviegame.security.controller.dto.RegisterRequest;
import com.ada.moviegame.security.domain.GameUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;
  private final GameUserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationResponse register(RegisterRequest request) {
    validatePassword(request);
    var user =
        GameUser.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .role(USER)
            .build();
    userService.saveGameUser(user);
    return getAuthenticationResponse(user);
  }

  private void validatePassword(RegisterRequest request) {
    if (!request.password().equals(request.passwordConfirmation())) {
      log.error("provided passwords do not match");
      throw new PasswordConfirmationException();
    }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    var user = userService.getGameUser(request.username());
    return getAuthenticationResponse(user);
  }

  private AuthenticationResponse getAuthenticationResponse(GameUser user) {
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }
}
