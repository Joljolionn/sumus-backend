package com.sumus.sumus_backend.mappers.impl;

import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.UserDto;
// NOVO IMPORT: Importa o modelo de MongoDB
import com.sumus.sumus_backend.domain.entities.UserDocuments;
import com.sumus.sumus_backend.mappers.Mapper;

// Classe para mapear DTO em Documento e vice-versa
@Component
// CORRIGIDO: Implementa Mapper<UserDocuments, UserDto>
public class UserMapper implements Mapper<UserDocuments, UserDto> {

    private PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    // CORRIGIDO: Tipo de retorno e objeto criado agora é UserDocuments
    public UserDocuments mapFrom(UserDto b) throws IOException{
        UserDocuments userDocument = new UserDocuments(); // Usa o construtor do novo modelo
        userDocument.setEmail(b.getEmail());
        userDocument.setUsername(b.getUsername());
        userDocument.setTelefone(b.getTelefone());
        userDocument.setPassword(passwordEncoder.encode(b.getPassword()));

        // Trata a foto se houver
        if (b.getFoto() != null) {
            userDocument.setContentType(b.getFoto().getContentType());
            userDocument.setFoto(b.getFoto().getBytes());
        }

        return userDocument;
    }

    @Override
    // CORRIGIDO: Argumento do método é UserDocuments
    public UserDto mapTo(UserDocuments a) {
        UserDto userDto = new UserDto();
        userDto.setEmail(a.getEmail());
        userDto.setUsername(a.getUsername());
        userDto.setTelefone(a.getTelefone());
        userDto.setContentType(a.getContentType());
        userDto.setFotoBytes(a.getFoto());
        return userDto;
    }

    @Override
    // CORRIGIDO: Argumento do método é UserDocuments
    public void updateEntityFromDto(UserDocuments entity, UserDto dto) {
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