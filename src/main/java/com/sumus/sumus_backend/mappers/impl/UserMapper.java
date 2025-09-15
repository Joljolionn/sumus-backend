package com.sumus.sumus_backend.mappers.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.mappers.Mapper;

@Component
public class UserMapper implements Mapper<UserEntity, UserDto> {

    private PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity mapFrom(UserDto b) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(b.getEmail());
        userEntity.setUsername(b.getUsername());
        userEntity.setTelefone(b.getTelefone());
        userEntity.setPassword(passwordEncoder.encode(b.getPassword()));
        return userEntity;
    }

    @Override
    public UserDto mapTo(UserEntity a) {
        UserDto userDto = new UserDto();
        userDto.setEmail(a.getEmail());
        userDto.setUsername(a.getUsername());
        userDto.setTelefone(a.getTelefone());
        return userDto;
    }

    @Override
    public void updateEntityFromDto(UserEntity entity, UserDto dto) {
        if (dto.getEmail() != null)
            entity.setEmail(dto.getEmail());
        if (dto.getUsername() != null)
            entity.setUsername(dto.getUsername());
        if (dto.getTelefone() != null)
            entity.setTelefone(dto.getTelefone());
        if (dto.getPassword() != null)
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

}
