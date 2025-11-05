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
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistrationDto;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
import com.sumus.sumus_backend.domain.entities.PassengerDocument;
import com.sumus.sumus_backend.repositories.PassengerRepository;
import com.sumus.sumus_backend.services.PassengerService;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private PassengerRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO: Fazer com que esse método retorne um DTO
    @Override
    public PassengerDocument create(PassengerRegistrationDto userDto) throws IOException {


        // TODO: Revisar se essa verificação já não é redundante com a regra de negócio do banco
        if (userRepository.existsByEmail(userDto.getEmail())) {
            // Lança uma exceção se o e-mail já estiver em uso, garantindo que a regra de
            // negócio seja respeitada.
            throw new IllegalArgumentException(
                    "Erro: O e-mail " + userDto.getEmail() + " já está cadastrado no sistema.");
        }

        PassengerDocument userDocument = new PassengerDocument(
                userDto.getName(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getPhone(),
                false,
                null
        );

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
    public List<PassengerDocument> listAll() {
        return userRepository.findAll();
    }

    // TODO: Fazer que esse método retorne um DTO
    @Override
    public Optional<PassengerDocument> update(PassengerRegistrationDto userDto) throws IOException {
        Optional<PassengerDocument> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            return user;
        }

        PassengerDocument updatedUser = user.get();

        if (userDto.getName() != null)
            updatedUser.setName(userDto.getName());
        if (userDto.getEmail() != null)
            updatedUser.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null)
            updatedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getPhone() != null)
            updatedUser.setPhone(userDto.getPhone());

        // Atualizar a foto

        if (userDto.getPhoto() != null && !userDto.getPhoto().isEmpty()) {
            if (updatedUser.getPhotoId() != null) {
                gridFsTemplate.delete(Query.query(Criteria.where("_id").is(updatedUser.getPhotoId())));
            }
            ObjectId fileId = gridFsTemplate.store(
                    userDto.getPhoto().getInputStream(),
                    userDto.getPhoto().getOriginalFilename(),
                    userDto.getPhoto().getContentType());

            updatedUser.setPhotoId(fileId);
        }

        return Optional.of(userRepository.save(updatedUser));
    }

    @Override
    public Boolean delete(String email) {
        Optional<PassengerDocument> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        } else {
            userRepository.deleteById(user.get().getId());
            return true;
        }
    }

    // TODO: Fazer que esse método retorne um DTO
    @Override
    public Optional<PassengerDocument> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {
        Optional<PassengerDocument> userOptional = userRepository.findByEmail(loginRequest.getEmail());

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
    public GridFsResource getPhotoResourceByPassengerEmail(String email) {
        Optional<PassengerDocument> found = userRepository.findByEmail(email);

        if (found.isEmpty()) {
            return null;
        }

        PassengerDocument user = found.get();

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
