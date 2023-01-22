package com.ada.moviegame.security.controller.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {}
