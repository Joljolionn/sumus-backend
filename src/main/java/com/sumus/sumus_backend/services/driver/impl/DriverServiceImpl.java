package com.sumus.sumus_backend.services.driver.impl;

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
import com.sumus.sumus_backend.domain.dtos.request.DriverRegistration;
import com.sumus.sumus_backend.domain.dtos.request.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.response.AuthResult;
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
    public DriverDocument create(DriverRegistration driverRegistration) throws IOException {

        DriverDocument driverDocument = new DriverDocument(driverRegistration.getName(), driverRegistration.getEmail(),
                passwordEncoder.encode(driverRegistration.getPassword()), driverRegistration.getPhone(), driverRegistration.getCnh());

        if (driverRegistration.getPhoto() != null && !driverRegistration.getPhoto().isEmpty()) {
            MultipartFile file = driverRegistration.getPhoto();

            ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType());

            driverDocument.setPhotoId(objectId);
        }

        return driverRepository.save(driverDocument);
    }

    @Override
    public List<DriverDocument> listAll() {
        return driverRepository.findAll();
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {
        Optional<DriverDocument> driverOptional = driverRepository.findByEmail(loginRequest.getEmail());

        if (driverOptional.isEmpty()) {
            return new AuthResult(AuthResult.Status.USER_NOT_FOUND, null);
        }
        if (passwordEncoder.matches(loginRequest.getPassword(), driverOptional.get().getPassword())) {
            return new AuthResult(AuthResult.Status.SUCCESS, "funcionou");
        }
        return new AuthResult(AuthResult.Status.INVALID_PASSWORD, null);
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
    public Optional<DriverDocument> findByEmail(String email) {
        return driverRepository.findByEmail(email);
    }

    @Override
    public DriverDocument verifyDocuments(String email) {
        Optional<DriverDocument> driverOptional = driverRepository.findByEmail(email);

        if (driverOptional.isEmpty()) {
            return null;
        }

        DriverDocument driverDocument = driverOptional.get();

        driverDocument.setIsActive(true);

        return driverRepository.save(driverDocument);

    }

}
