package com.ada.moviegame.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTService {

  private static String PRIVATE_KEY =
      "462D4A614E645267556B58703273357638782F413F4428472B4B625065536856";
  private static Long EXPIRATION_TIME = (long) (1000 * 60 * 60 * 24);

  public String extractUsername(String jwtToken) {
    return extractClaim(jwtToken, Claims::getSubject);
  }

  private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(jwtToken);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(getExpirationDate())
        .signWith(SignatureAlgorithm.HS256, getSigningKey())
        .compact();
  }

  public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
    final String username = extractUsername(jwtToken);
    return username != null
        && username.equals(userDetails.getUsername())
        && !isTokenExpired(jwtToken);
  }

  private boolean isTokenExpired(String jwtToken) {
    return extractExpiration(jwtToken).before(new Date());
  }

  private Date extractExpiration(String jwtToken) {
    return extractClaim(jwtToken, Claims::getExpiration);
  }

  private static Date getExpirationDate() {
    return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
  }

  public Claims extractAllClaims(String jwtToken) {
    return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(jwtToken).getBody();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(PRIVATE_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
