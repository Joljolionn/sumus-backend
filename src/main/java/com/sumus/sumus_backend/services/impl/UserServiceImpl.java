package com.sumus.sumus_backend.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserDocuments;
import com.sumus.sumus_backend.mappers.impl.UserMapper;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.services.UserService;


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
    public UserDocuments create(UserDto userDto) throws IOException {


        if (userRepository.existsByEmail(userDto.getEmail())) {
            // Lança uma exceção se o e-mail já estiver em uso, garantindo que a regra de negócio seja respeitada.
            throw new IllegalArgumentException("Erro: O e-mail " + userDto.getEmail() + " já está cadastrado no sistema.");
        }


        UserDocuments userDocument = userMapper.mapFrom(userDto);
        return userRepository.save(userDocument);
    }

    @Override
    public List<UserDocuments> listAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserDocuments> update(UserDto userDto) {
        Optional<UserDocuments> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            return user;
        }

        UserDocuments updatedUser = user.get();
        userMapper.updateEntityFromDto(updatedUser, userDto);

        return Optional.of(userRepository.save(updatedUser));
    }

    @Override
    public Boolean delete(String email) {
        Optional<UserDocuments> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        } else {
            userRepository.deleteById(user.get().getId());
            return true;
        }
    }

    @Override
    public Optional<UserDocuments> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {
        Optional<UserDocuments> userOptional = userRepository.findByEmail(loginRequest.getEmail());

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