package com.ada.moviegame.security.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(@NotBlank String username, @NotBlank String password) {}
