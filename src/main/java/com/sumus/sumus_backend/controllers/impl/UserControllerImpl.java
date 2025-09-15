package com.sumus.sumus_backend.controllers.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.services.UserService;

@RestController
public class UserControllerImpl {
    private UserService userService;

    UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(userService.create(userEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        AuthResult authResult = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        HttpStatus httpStatus = HttpStatus.OK;
        if (authResult.getStatus() == AuthResult.Status.USER_NOT_FOUND) {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        if (authResult.getStatus() == AuthResult.Status.INVALID_PASSWORD) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(authResult.getToken(), httpStatus);
    }
}
