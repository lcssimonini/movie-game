package com.ada.moviegame.security.configuration;

import com.ada.moviegame.security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  public static final String AUTH_TOKEN_PREFIX = "Bearer ";
  public static final int AUTH_TOKEN_BEGIN_INDEX = 7;
  private final UserDetailsService userDetailsService;
  private final JWTService jwtService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
    final String username;
    final String jwtToken;

    if (authHeaderNotPovided(authHeader)) {
      filterChain.doFilter(request, response);
      return;
    }

    jwtToken = getJwtToken(authHeader);
    username = jwtService.extractUsername(jwtToken);
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (jwtService.isTokenValid(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }

  private static String getJwtToken(String authHeader) {
    return authHeader.substring(AUTH_TOKEN_BEGIN_INDEX);
  }

  private static boolean authHeaderNotPovided(String authHeader) {
    return authHeader == null || !authHeader.startsWith(AUTH_TOKEN_PREFIX);
  }
}
