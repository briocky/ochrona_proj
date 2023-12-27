package pl.edu.pw.ee.secureloansystem.infrastructure.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenUtils {

  @Value("${security.jwt.secret}")
  private String base64EncodedSecretKey;

  @Value("${security.jwt.accessToken.expiration}")
  private long tokenExpirationMilis;

  @Value("${security.jwt.refreshToken.expiration}")
  private long refreshTokenExpirationMilis;

  public String generateAccessToken(UserDetails userDetails) {
    return generateToken(userDetails, tokenExpirationMilis);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return generateToken(userDetails, refreshTokenExpirationMilis);
  }

  private String generateToken(UserDetails userDetails, long expirationMilis) {
    return Jwts
      .builder()
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(new Date(new Date().getTime() + expirationMilis))
      .signWith(getSigningKey())
      .compact();
  }

  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims;
    try {
      claims =
      Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    } catch (ExpiredJwtException ex) {
      claims = ex.getClaims();
    }
    return claimsResolver.apply(claims);
  }

  public boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  private Key getSigningKey() {
    byte[] decoded = Decoders.BASE64.decode(base64EncodedSecretKey);
    return Keys.hmacShaKeyFor(decoded);
  }
}
