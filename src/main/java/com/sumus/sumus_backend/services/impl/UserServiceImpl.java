package com.sumus.sumus_backend.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.request.UserRegistrationDto;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.mappers.impl.UserMapper;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    // TODO: Fazer com que esse método retorne um DTO
    @Override
    public UserDocument create(UserRegistrationDto userDto) throws IOException {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            // Lança uma exceção se o e-mail já estiver em uso, garantindo que a regra de
            // negócio seja respeitada.
            throw new IllegalArgumentException(
                    "Erro: O e-mail " + userDto.getEmail() + " já está cadastrado no sistema.");
        }

        UserDocument userDocument = userMapper.mapFrom(userDto);

        if (userDto.getPhoto() != null && !userDto.getPhoto().isEmpty()) {
            MultipartFile file = userDto.getPhoto();

            ObjectId fileId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType());

            userDocument.setPhotoId(fileId);
        }

        return userRepository.save(userDocument);
    }

    @Override
    public List<UserDocument> listAll() {
        return userRepository.findAll();
    }

    // TODO: Fazer que esse método retorne um DTO
    @Override
    public Optional<UserDocument> update(UserRegistrationDto userDto) {
        Optional<UserDocument> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            return user;
        }

        UserDocument updatedUser = user.get();
        userMapper.updateEntityFromDto(updatedUser, userDto);

        return Optional.of(userRepository.save(updatedUser));
    }

    @Override
    public Boolean delete(String email) {
        Optional<UserDocument> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        } else {
            userRepository.deleteById(user.get().getId());
            return true;
        }
    }

    // TODO: Fazer que esse método retorne um DTO
    @Override
    public Optional<UserDocument> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {
        Optional<UserDocument> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return new AuthResult(AuthResult.Status.USER_NOT_FOUND, null);
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            return new AuthResult(AuthResult.Status.SUCCESS, "funcionou");
        } else {
            return new AuthResult(AuthResult.Status.INVALID_PASSWORD, null);
        }
    }

    @Override
    public GridFsResource getPhotoResourceByUserEmail(String email) {
        Optional<UserDocument> found = userRepository.findByEmail(email);

        if (found.isEmpty()) {
            return null;
        }

        UserDocument user = found.get();

        if (user.getPhotoId() == null) {
            return null;
        }

        GridFSFile file = gridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(user.getPhotoId())));

        if (file == null) {
            return null;
        }

        return gridFsTemplate.getResource(file);
    }

}
