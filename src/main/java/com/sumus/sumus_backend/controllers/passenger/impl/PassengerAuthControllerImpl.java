package com.sumus.sumus_backend.controllers.passenger.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.controllers.passenger.docs.PassengerAuthControllerDocs;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;
import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;
import com.sumus.sumus_backend.services.passenger.PassengerService;

import jakarta.validation.Valid;

// Classe para representar os endpoints da aplicação
@RestController
@RequestMapping("/passenger")
public class PassengerAuthControllerImpl implements PassengerAuthControllerDocs {

    @Autowired
    private PassengerService userService;

    @Autowired
    @Qualifier("passengerAuthenticationProvider") // Para garantir que o Bean de provedor
                                                  // utilizado será o especificado
                                                  // para lidar com motoristas

    private DaoAuthenticationProvider passengerAuthenticationProvider;

    @Override
    @PostMapping(path = "/signup")
    public ResponseEntity<PassengerDocument> createPassenger(@ModelAttribute @Valid PassengerRegistration userDto) {
        PassengerDocument user;
        try {
            user = userService.create(userDto);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Override
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());

        Authentication auth = passengerAuthenticationProvider.authenticate(usernamePassword);

        return ResponseEntity.ok("Funcionou");
    }
}
