package com.sumus.sumus_backend.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Importação do modelo de dados do MongoDB que criamos!
import com.sumus.sumus_backend.domain.entities.UserDocuments;
import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;


public interface UserService {

    // CORREÇÃO DE IMPORTS: Se o UserDocuments não estiver importado, o Java não o encontra.
    UserDocuments create(UserDto userDto) throws IOException;

    List<UserDocuments> listAll();

    Optional<UserDocuments> update(UserDto userDto);

    Boolean delete(String email);

    Optional<UserDocuments> findByEmail(String email);

    AuthResult login(LoginRequest loginRequest);
}