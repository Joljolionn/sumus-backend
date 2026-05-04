package com.sumus.auth_service.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

@RestController
public class JwksController {

    @Autowired
    private RSAKey rsaKey;


    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwks() {
        return new JWKSet(this.rsaKey.toPublicJWK()).toJSONObject();
    }
}
