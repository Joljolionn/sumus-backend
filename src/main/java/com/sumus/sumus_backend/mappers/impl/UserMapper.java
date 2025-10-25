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

    // Mapeia um RegisterDTO para entidade do banco
    @Override
    public UserDocument mapFrom(UserRegistrationDto b) throws IOException{
        UserDocument userDocument = new UserDocument(); 
        userDocument.setEmail(b.getEmail());
        userDocument.setName(b.getName());
        userDocument.setPhone(b.getPhone());
        userDocument.setPassword(passwordEncoder.encode(b.getPassword()));

        return userDocument;
    }

    // Mapeia uma entidade do banco para ResponseDTO
    @Override
    public UserRegistrationDto mapTo(UserDocument a) {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail(a.getEmail());
        userDto.setName(a.getName());
        userDto.setPhone(a.getPhone());
        return userDto;
    }

    // Atualiza uma entidade com base em um RegisterDTO
    @Override
    public void updateEntityFromDto(UserDocument entity, UserRegistrationDto dto) {
        if (dto.getName() != null)
            entity.setName(dto.getName());
        if (dto.getEmail() != null)
            entity.setEmail(dto.getEmail());
        if (dto.getPassword() != null)
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getPhone() != null)
            entity.setPhone(dto.getPhone());
    }

}
