package com.sumus.sumus_backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity create(UserEntity userEntity) {

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> listAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        return userRepository.save(userEntity);
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
    public AuthResult login(String email, String senha) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new AuthResult(AuthResult.Status.USER_NOT_FOUND, null);
        }

        if (passwordEncoder.matches(senha, userOptional.get().getPassword())) {
            return new AuthResult(AuthResult.Status.SUCCESS, "funcionou");
        } else {
            return new AuthResult(AuthResult.Status.INVALID_PASSWORD, null);
        }
    }

}
