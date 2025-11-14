package com.sumus.sumus_backend.controllers.passenger.impl;

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

import com.sumus.sumus_backend.controllers.passenger.docs.PassengerAuthControllerDocs;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;
import com.sumus.sumus_backend.domain.dtos.response.AuthResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.PassengerResponseDto;
import com.sumus.sumus_backend.infra.security.jwt.JwtService;
import com.sumus.sumus_backend.infra.security.util.UserRole;
import com.sumus.sumus_backend.services.passenger.PassengerService;

import jakarta.validation.Valid;

// Classe para representar os endpoints da aplicação
@RestController
@RequestMapping("/passenger")
public class PassengerAuthControllerImpl implements PassengerAuthControllerDocs {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    @Qualifier("passengerAuthenticationProvider") // Para garantir que o Bean de provedor
                                                  // utilizado será o especificado
                                                  // para lidar com motoristas

    private DaoAuthenticationProvider passengerAuthenticationProvider;

    @Override
    @PostMapping(path = "/signup")
    public ResponseEntity<PassengerResponseDto> createPassenger(@ModelAttribute @Valid PassengerRegistration passengerRegistration) {
        PassengerResponseDto passengerResponseDto;
        try {
            passengerResponseDto = passengerService.create(passengerRegistration);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(passengerResponseDto, HttpStatus.CREATED);
    }

    @Override
    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());

        Authentication auth = passengerAuthenticationProvider.authenticate(usernamePassword);

        String token = jwtService.generateToken((UserDetails) auth.getPrincipal(), UserRole.PASSENGER);

        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
