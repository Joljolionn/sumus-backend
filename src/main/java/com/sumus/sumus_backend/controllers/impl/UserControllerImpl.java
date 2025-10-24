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
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.controllers.docs.UserControllerDocs;
import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;
// ADICIONADO: Importação do novo modelo de dados do MongoDB!
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.services.UserService;

// Classe para representar os endpoints da aplicação
@RestController
public class UserControllerImpl implements UserControllerDocs {
    
    @Autowired
    private UserService userService;

    @PostMapping(path = "/signup")
    // CORRIGIDO: Tipo de retorno agora é UserDocument
    public ResponseEntity<UserDocument> createUser(@ModelAttribute UserDto userDto) {
        // CORRIGIDO: Variável local agora é UserDocument
        UserDocument user;
        try {
            // O serviço retorna UserDocument
            user = userService.create(userDto);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    // CORRIGIDO: Tipo de retorno agora é List<UserDocument>
    public ResponseEntity<List<UserDocument>> getAllUsers() {
        // O serviço retorna List<UserDocument>
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
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
