package com.sumus.sumus_backend.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.gridfs.GridFsResource;

// Importação do modelo de dados do MongoDB que criamos!
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;


public interface UserService {

    // CORREÇÃO DE IMPORTS: Se o UserDocument não estiver importado, o Java não o encontra.
    UserDocument create(UserDto userDto) throws IOException;

    List<UserDocument> listAll();

    Optional<UserDocument> update(UserDto userDto);

    Boolean delete(String email);

    Optional<UserDocument> findByEmail(String email);

    AuthResult login(LoginRequest loginRequest);

    GridFsResource getPhotoResourceByUserEmail(String email);
}
