package com.sumus.sumus_backend.controllers.driver.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.domain.dtos.request.DriverRegistration;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.response.AuthResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.DriverResponseDto;
import com.sumus.sumus_backend.infra.security.jwt.JwtService;
import com.sumus.sumus_backend.infra.security.util.UserRole;
import com.sumus.sumus_backend.services.driver.DriverService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/driver")
public class DriverAuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DriverService driverService;

    @Autowired
    @Qualifier("driverAuthenticationProvider") // Para garantir que o Bean de provedor
                                               // utilizado será o especificado
                                               // para lidar com motoristas
    private DaoAuthenticationProvider driverAuthenticationProvider;

    @PostMapping(path = "/signup")
    public ResponseEntity<DriverResponseDto> createPassenger(
            @ModelAttribute @Valid DriverRegistration driverRegistration) {
        DriverResponseDto driverResponseDto;
        try {
            driverResponseDto = driverService.create(driverRegistration);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(driverResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());

        Authentication auth = driverAuthenticationProvider.authenticate(usernamePassword);

        String token = jwtService.generateToken((UserDetails) auth.getPrincipal(), UserRole.DRIVER);

        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
