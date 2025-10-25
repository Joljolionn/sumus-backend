package com.sumus.sumus_backend.mappers.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.request.UserRegistrationDto;
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.mappers.Mapper;

@Component
public class UserMapper implements Mapper<UserDocument, UserRegistrationDto> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDocument mapFrom(UserRegistrationDto b) throws IOException{
        UserDocument userDocument = new UserDocument(); 
        userDocument.setEmail(b.getEmail());
        userDocument.setName(b.getUsername());
        userDocument.setPhone(b.getTelefone());
        userDocument.setPassword(passwordEncoder.encode(b.getPassword()));

        return userDocument;
    }

    @Override
    public UserRegistrationDto mapTo(UserDocument a) {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail(a.getEmail());
        userDto.setUsername(a.getName());
        userDto.setTelefone(a.getPhone());
        return userDto;
    }

    @Override
    public void updateEntityFromDto(UserDocument entity, UserRegistrationDto dto) {
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
