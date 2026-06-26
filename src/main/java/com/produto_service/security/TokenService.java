package com.produto_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var verificador = JWT
                    .require(algorithm)
                    .withIssuer("ClienteService")
                    .build()
                    .verify(token);
            String subject = verificador.getSubject();
            if(subject == null || subject.isEmpty()) {
                subject = verificador.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress").asString();
            }
            return subject;
        } catch (JWTVerificationException e) {
            System.err.println("Erro ao validar token JWT: " + e.getMessage());
            return "";
        }
    }

    public String getRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var verificador = JWT.require(algorithm).withIssuer("ClienteService").build().verify(token);
            var roleClaim = verificador.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
            return roleClaim.isNull() ? "CLIENT" : roleClaim.asString();
        } catch (Exception e) {
            return "CLIENT";
        }
    }
}
