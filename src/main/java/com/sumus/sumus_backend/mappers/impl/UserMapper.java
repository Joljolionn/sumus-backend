package com.sumus.sumus_backend.mappers.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.mappers.Mapper;

@Component
public class UserMapper implements Mapper<UserDocument, UserDto> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDocument mapFrom(UserDto b) throws IOException{
        UserDocument userDocument = new UserDocument(); 
        userDocument.setEmail(b.getEmail());
        userDocument.setName(b.getUsername());
        userDocument.setPhone(b.getTelefone());
        userDocument.setPassword(passwordEncoder.encode(b.getPassword()));

        return userDocument;
    }

    @Override
    public UserDto mapTo(UserDocument a) {
        UserDto userDto = new UserDto();
        userDto.setEmail(a.getEmail());
        userDto.setUsername(a.getName());
        userDto.setTelefone(a.getPhone());
        return userDto;
    }

    @Override
    public void updateEntityFromDto(UserDocument entity, UserDto dto) {
        if (dto.getEmail() != null)
            entity.setEmail(dto.getEmail());
        if (dto.getUsername() != null)
            entity.setName(dto.getUsername());
        if (dto.getTelefone() != null)
            entity.setPhone(dto.getTelefone());
        if (dto.getPassword() != null)
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

}
