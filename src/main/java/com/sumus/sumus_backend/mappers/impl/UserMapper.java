package com.sumus.sumus_backend.mappers.impl;

import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.mappers.Mapper;

public class UserMapper implements Mapper<UserEntity, UserDto> {

    @Override
    public UserEntity mapFrom(UserDto b) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(b.getEmail());
        userEntity.setUsername(b.getUsername());
        userEntity.setTelefone(b.getTelefone());
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

}
