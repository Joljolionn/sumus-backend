package com.sumus.sumus_backend.controllers.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.entities.UserEntity;
import com.sumus.sumus_backend.services.UserService;

@RestController
public class UserControllerImpl {
    private UserService userService;

    UserControllerImpl(UserService userService){
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity){
        return new ResponseEntity<>(userService.create(userEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }
}
