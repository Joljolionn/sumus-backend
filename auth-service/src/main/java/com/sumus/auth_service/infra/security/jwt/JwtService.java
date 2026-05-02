package com.sumus.auth_service.infra.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.sumus.auth_service.infra.security.util.UserRole;

@Service
public class JwtService {

  @Autowired
  private RSAKey key;

  private Algorithm getAlgorithm() throws IllegalArgumentException, JOSEException {
    return Algorithm.RSA256(key.toRSAPublicKey(), key.toRSAPrivateKey());
  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    // Token expira em 2 horas
  }

  private String issuer = "http://auth-service:8080";

  public String generateToken(UserDetails userDetails, UserRole userRole)
      throws IllegalArgumentException, JOSEException {

    try {
      Algorithm algorithm = getAlgorithm();

      String token = JWT.create()
          .withIssuer(issuer)
          .withSubject(userDetails.getUsername())
          .withClaim("role_type", userRole.getAuthority())
          .withExpiresAt(generateExpirationDate())
          .sign(algorithm);
      return token;
    } catch (JWTCreationException e) {
      throw new RuntimeException("Error while generating token", e);
    }

  }

  public String validateToken(String token) throws IllegalArgumentException, JOSEException {
    try {

      Algorithm algorithm = getAlgorithm();
      return JWT.require(algorithm)
          .withIssuer(issuer)
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException e) {
      return null;
    }
  }

  public Claim extractClaim(String token, String claimName)
      throws IllegalArgumentException, JOSEException {
    try {
      return decodeToken(token).getClaim(claimName);
    } catch (JWTVerificationException e) {
      return null;
    }
  }


  private DecodedJWT decodeToken(String token)
      throws JWTVerificationException, IllegalArgumentException, JOSEException {

    return JWT.require(getAlgorithm())
        .withIssuer(issuer)
        .build()
        .verify(token);
  }
}
