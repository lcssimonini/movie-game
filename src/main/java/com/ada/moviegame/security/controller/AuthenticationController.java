package com.ada.moviegame.security.controller;

import com.ada.moviegame.security.controller.dto.AuthenticationRequest;
import com.ada.moviegame.security.controller.dto.AuthenticationResponse;
import com.ada.moviegame.security.controller.dto.RegisterRequest;
import com.ada.moviegame.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public AuthenticationResponse register(@Valid @RequestBody RegisterRequest request) {
    return authenticationService.register(request);
  }

  @PostMapping("/authenticate")
  public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
    return authenticationService.authenticate(request);
  }
}
