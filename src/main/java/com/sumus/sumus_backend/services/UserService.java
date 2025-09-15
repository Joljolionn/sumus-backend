package com.sumus.sumus_backend.services;

import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserEntity;


public interface UserService {
    
    UserEntity create(UserDto userDto);

    List<UserEntity> listAll();

    Optional<UserEntity> update(UserDto userDto);

    Boolean delete(String email);

    Optional<UserEntity> findByEmail(String email);

    AuthResult login(LoginRequest loginRequest);
}
