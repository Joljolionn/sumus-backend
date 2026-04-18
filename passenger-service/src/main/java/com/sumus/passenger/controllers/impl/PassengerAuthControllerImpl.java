package com.sumus.passenger.controllers.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.passenger.controllers.docs.PassengerAuthControllerDocs;
import com.sumus.passenger.domain.dtos.request.LoginRequest;
import com.sumus.passenger.domain.dtos.request.PassengerRegistrationRequest;
import com.sumus.passenger.domain.dtos.response.AuthResponseDto;
import com.sumus.passenger.domain.dtos.response.PassengerResponseDto;
import com.sumus.passenger.infra.security.jwt.JwtService;
import com.sumus.passenger.infra.security.util.UserRole;
import com.sumus.passenger.services.PassengerService;

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
  @Qualifier("passengerAuthenticationProvider")
  private DaoAuthenticationProvider passengerAuthenticationProvider;

  @Override
  @PostMapping(path = "/signup")
  public ResponseEntity<PassengerResponseDto> createPassenger(
      @RequestBody @Valid PassengerRegistrationRequest passengerRegistration) {
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
    try {

      UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
          loginRequest.getEmail(), loginRequest.getPassword());

      Authentication auth = passengerAuthenticationProvider.authenticate(usernamePassword);

      String token = jwtService.generateToken((UserDetails) auth.getPrincipal(), UserRole.PASSENGER);

      return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(token));

    } catch (BadCredentialsException e) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    } catch (UsernameNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    } catch (AuthenticationException e) {

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
  }
}
