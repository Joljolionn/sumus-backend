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
import org.springframework.web.bind.annotation.RestController;

import com.sumus.passenger.controllers.docs.PassengerAuthControllerDocs;
import com.sumus.passenger.domain.dtos.request.LoginRequest;
import com.sumus.passenger.domain.dtos.request.PassengerRegistrationRequest;
import com.sumus.passenger.domain.dtos.response.AuthResponseDto;
import com.sumus.passenger.domain.dtos.response.PassengerResponseDto;
import com.sumus.passenger.infra.security.jwt.JwtService;
import com.sumus.passenger.infra.security.util.UserRole;
import com.sumus.passenger.services.PassengerService;

@RestController
public class PassengerAuthControllerImpl implements PassengerAuthControllerDocs {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private PassengerService passengerService;

  @Autowired
  @Qualifier("passengerAuthenticationProvider")
  private DaoAuthenticationProvider passengerAuthenticationProvider;

  @Override
  public ResponseEntity<PassengerResponseDto> postSignup(
      PassengerRegistrationRequest passengerRegistration) {
    PassengerResponseDto passengerResponseDto;
    try {
      passengerResponseDto = passengerService.create(passengerRegistration);
    } catch (IOException e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(passengerResponseDto, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<AuthResponseDto> postLogin(LoginRequest loginRequest) {
    try {

      UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
          loginRequest.email(), loginRequest.password());

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
