package com.sumus.sumus_backend.infra.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sumus.sumus_backend.infra.security.util.UserRole;

@Service
public class JwtService {

    /* Variável de ambiente de segredo para criptografia de tokens */
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserDetails userDetails, UserRole userRole) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("sumus_backend")
                    .withSubject(userDetails.getUsername())
                    .withClaim("role_type", userRole.getAuthority())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }

    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("sumus_backend")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public Claim extractClaim(String token, String claimName) {
        try {
            return decodeToken(token).getClaim(claimName);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        // Token expira em 2 horas
    }

    private DecodedJWT decodeToken(String token) throws JWTVerificationException {
        // Tenta decodificar o token, verificando Assinatura, Emissor e Expiração.
        return JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("sumus_backend")
                .build()
                .verify(token);
    }
}
