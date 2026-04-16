package com.sumus.sumus_backend.services.driver.impl;

import java.io.IOException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sumus.sumus_backend.domain.dtos.request.DriverRegistration;
import com.sumus.sumus_backend.domain.dtos.request.DriverUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.response.DriverListResponseDTO;
import com.sumus.sumus_backend.domain.dtos.response.DriverResponseDto;
import com.sumus.sumus_backend.domain.entities.driver.DriverDocument;
import com.sumus.sumus_backend.repositories.driver.DriverRepository;
import com.sumus.sumus_backend.services.driver.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public DriverResponseDto create(DriverRegistration driverRegistration) throws IOException {

        if (driverRepository.existsByEmail(driverRegistration.getEmail())) {
            // Lança uma exceção se o e-mail já estiver em uso, garantindo que a regra de
            // negócio seja respeitada.
            throw new IllegalArgumentException(
                    "Erro: O e-mail " + driverRegistration.getEmail() + " já está cadastrado no sistema.");
        }

        DriverDocument driverDocument = new DriverDocument(driverRegistration.getName(), driverRegistration.getEmail(),
                passwordEncoder.encode(driverRegistration.getPassword()), driverRegistration.getPhone(),
                driverRegistration.getCnh());

        driverDocument = driverRepository.save(driverDocument);

        return new DriverResponseDto(driverDocument);
    }

    @Override
    public DriverListResponseDTO listAll() {
        return new DriverListResponseDTO(driverRepository.findAll());
    }

    @Override
    public GridFsResource getPhotoResourceByDriverEmail(String email) {
        Optional<DriverDocument> driverOptional = driverRepository.findByEmail(email);

        if (driverOptional.isEmpty()) {
            return null;
        }

        DriverDocument driverDocument = driverOptional.get();

        if (driverDocument.getPhotoId() == null) {
            return null;
        }

        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(driverDocument.getPhotoId())));

        if (file == null) {
            return null;
        }

        return gridFsTemplate.getResource(file);
    }

    @Override
    public DriverResponseDto findByEmail(String email) {
        Optional<DriverDocument> driverDocument = driverRepository.findByEmail(email);

        if (driverDocument.isEmpty()) {
            return null;
        }

        return new DriverResponseDto(driverDocument.get());
    }

    @Override
    public DriverResponseDto verifyDocuments(String email) {
        Optional<DriverDocument> driverOptional = driverRepository.findByEmail(email);

        if (driverOptional.isEmpty()) {
            return null;
        }

        DriverDocument driverDocument = driverOptional.get();

        driverDocument.setIsActive(true);

        driverDocument = driverRepository.save(driverDocument);

        return new DriverResponseDto(driverDocument);

    }

    @Override
    public DriverResponseDto update(String email, DriverUpdateRequest driverUpdateRequest) throws IOException {

        Optional<DriverDocument> driverOptional = driverRepository
                .findByEmail(email);
        if (driverOptional.isEmpty()) {
            return null;
        }

        DriverDocument driverDocument = driverOptional.get();

        if (driverUpdateRequest.getName() != null)
            driverDocument.setName(driverUpdateRequest.getName());
        if (driverUpdateRequest.getEmail() != null)
            driverDocument.setEmail(driverUpdateRequest.getEmail());
        if (driverUpdateRequest.getPhone() != null)
            driverDocument.setPhone(driverUpdateRequest.getPhone());
        if (driverUpdateRequest.getCnh() != null)
            driverDocument.setCnh(driverUpdateRequest.getCnh());

        // Atualizar a foto
        if (driverUpdateRequest.getPhoto() != null && !driverUpdateRequest.getPhoto().isEmpty()) {
            if (driverDocument.getPhotoId() != null) {
                gridFsTemplate.delete(Query.query(Criteria.where("_id").is(driverDocument.getPhotoId())));
            }
            ObjectId fileId = gridFsTemplate.store(
                    driverUpdateRequest.getPhoto().getInputStream(),
                    driverUpdateRequest.getPhoto().getOriginalFilename(),
                    driverUpdateRequest.getPhoto().getContentType());

            driverDocument.setPhotoId(fileId);
        }

        return new DriverResponseDto(driverRepository.save(driverDocument));
    }

    @Override
    public Boolean updatePassword(String email, PasswordUpdateRequest passwordUpdateRequest) {
        Optional<DriverDocument> driverOptional = driverRepository.findByEmail(email);

        if (driverOptional.isEmpty()) {
            return false;
        }

        DriverDocument driverDocument = driverOptional.get();

        driverDocument.setPassword(passwordEncoder.encode(passwordUpdateRequest.getPassword()));

        driverRepository.save(driverDocument);

        return true;
    }

    @Override
    public Boolean deleteDriver(String email) {
        Optional<DriverDocument> driverOptional = driverRepository.findByEmail(email);

        if (driverOptional.isEmpty()) {
            return false;
        }

        driverRepository.deleteById(driverOptional.get().getId());
        return true;

    }

}
