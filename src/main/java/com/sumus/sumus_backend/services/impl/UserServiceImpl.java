package com.sumus.sumus_backend.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.mappers.impl.UserMapper;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.services.UserService;

// Classe com responsabilidade de tratar a lógica em si do precessamento dos
// dados, mediadora entre a camada do banco de dados e a camada de apresentação
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity create(UserDto userDto) throws IOException {

        UserEntity userEntity = userMapper.mapFrom(userDto);

        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> listAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> update(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            return user;
        }

        UserEntity updatedUser = user.get();
        userMapper.updateEntityFromDto(updatedUser, userDto);

        return Optional.of(userRepository.save(updatedUser));
    }

    @Override
    public Boolean delete(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        } else {
            userRepository.deleteById(user.get().getId());
            return true;
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return new AuthResult(AuthResult.Status.USER_NOT_FOUND, null);
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            return new AuthResult(AuthResult.Status.SUCCESS, "funcionou");
        } else {
            return new AuthResult(AuthResult.Status.INVALID_PASSWORD, null);
        }
    }

}
