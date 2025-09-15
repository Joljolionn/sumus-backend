package com.sumus.sumus_backend.services;

import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.entities.UserEntity;


public interface UserService {
    
    UserEntity create(UserEntity userEntity);

    List<UserEntity> listAll();

    UserEntity update(UserEntity userEntity);

    Boolean delete(String email);

    Optional<UserEntity> findByEmail(String email);

    AuthResult login(String email, String senha);
}
