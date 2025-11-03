package com.sumus.sumus_backend.controllers.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.controllers.docs.AuthControllerDocs;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.UserRegistrationDto;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.services.UserService;

import jakarta.validation.Valid;

// Classe para representar os endpoints da aplicação
@RestController
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthControllerDocs {
    
    @Autowired
    private UserService userService;

    @Override
    @PostMapping(path = "/signup")
    public ResponseEntity<UserDocument> createUser(@ModelAttribute @Valid UserRegistrationDto userDto) {
        UserDocument user;
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
        AuthResult authResult = userService.login(loginRequest);
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
