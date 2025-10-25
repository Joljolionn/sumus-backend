package com.sumus.sumus_backend.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.UserRegistrationDto;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
// Importação do modelo de dados do MongoDB que criamos!
import com.sumus.sumus_backend.domain.entities.UserDocument;


public interface UserService {

    // CORREÇÃO DE IMPORTS: Se o UserDocument não estiver importado, o Java não o encontra.
    UserDocument create(UserRegistrationDto userDto) throws IOException;

    List<UserDocument> listAll();

    Optional<UserDocument> update(UserRegistrationDto userDto);

    Boolean delete(String email);

    Optional<UserDocument> findByEmail(String email);

    AuthResult login(LoginRequest loginRequest);

    GridFsResource getPhotoResourceByUserEmail(String email);
}
