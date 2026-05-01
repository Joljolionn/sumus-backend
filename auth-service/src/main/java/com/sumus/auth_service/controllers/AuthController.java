package com.sumus.auth_service.controllers;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.nimbusds.jose.JOSEException;
import com.sumus.auth_service.domain.dtos.AuthResponseDto;
import com.sumus.auth_service.domain.dtos.LoginRequest;
import com.sumus.auth_service.infra.security.jwt.JwtService;
import com.sumus.auth_service.infra.security.util.UserRole;

@Controller
public class AuthController {

  @Autowired
  private JwtService jwtService;


  @Autowired
  @Qualifier("passengerAuthenticationProvider")
  private DaoAuthenticationProvider passengerAuthenticationProvider;


  @Autowired
  @Qualifier("driverAuthenticationProvider")
  private DaoAuthenticationProvider driverAuthenticationProvider;


  @PostMapping(path = "/passenger/login")
  public ResponseEntity<AuthResponseDto> postPassengerLogin(@RequestBody
  LoginRequest loginRequest) throws IllegalArgumentException, JOSEException {
    try {

      UsernamePasswordAuthenticationToken usernamePassword =
          new UsernamePasswordAuthenticationToken(
              loginRequest.email(), loginRequest.password());

      Authentication auth = passengerAuthenticationProvider.authenticate(usernamePassword);

      String token =
          jwtService.generateToken((UserDetails) auth.getPrincipal(), UserRole.PASSENGER);

      return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(token));

    } catch (BadCredentialsException e) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    } catch (UsernameNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    } catch (AuthenticationException e) {

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
  }


  @PostMapping(path = "/driver/login")
  public ResponseEntity<AuthResponseDto> postDriverLogin(@RequestBody
  LoginRequest loginRequest) throws IllegalArgumentException, JOSEException {
    try {

      UsernamePasswordAuthenticationToken usernamePassword =
          new UsernamePasswordAuthenticationToken(
              loginRequest.email(), loginRequest.password());

      Authentication auth = driverAuthenticationProvider.authenticate(usernamePassword);

      String token = jwtService.generateToken((UserDetails) auth.getPrincipal(), UserRole.DRIVER);

      return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(token));

    } catch (BadCredentialsException e) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    } catch (UsernameNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    } catch (AuthenticationException e) {

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    } catch (Exception e) {
      throw e;
    }


  }
}
