package com.sumus.sumus_backend.services.passenger.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;
import com.sumus.sumus_backend.domain.dtos.response.PassengerListResponseDto;
import com.sumus.sumus_backend.domain.dtos.response.PassengerResponseDto;
import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;
import com.sumus.sumus_backend.domain.entities.passenger.PcdCondition;
import com.sumus.sumus_backend.repositories.passenger.PassengerRepository;
import com.sumus.sumus_backend.services.passenger.PassengerService;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PassengerResponseDto create(PassengerRegistration passengerRegistration) throws IOException {

 
        if (passengerRepository.existsByEmail(passengerRegistration.getEmail())) {
            // Lança uma exceção se o e-mail já estiver em uso, garantindo que a regra de
            // negócio seja respeitada.
            throw new IllegalArgumentException(
                    "Erro: O e-mail " + passengerRegistration.getEmail() + " já está cadastrado no sistema.");
        }

        List<PcdCondition> pcdConditions = null;

        if (passengerRegistration.getIsPcd()) {
            if (passengerRegistration.getConditions().isEmpty()) {
                throw new IllegalArgumentException("Erro: O usuário PCD deve informar suas condições");
            }
            pcdConditions = new ArrayList<PcdCondition>();

            for (String condition : passengerRegistration.getConditions()) {
                pcdConditions.add(new PcdCondition(condition));
            }
        }

        PassengerDocument passengerDocument = new PassengerDocument(
                passengerRegistration.getName(),
                passengerRegistration.getEmail(),
                passwordEncoder.encode(passengerRegistration.getPassword()),
                passengerRegistration.getPhone(),
                passengerRegistration.getIsPcd(),
                pcdConditions);

        if (passengerRegistration.getPhoto() != null && !passengerRegistration.getPhoto().isEmpty()) {
            MultipartFile file = passengerRegistration.getPhoto();

            ObjectId fileId = gridFsTemplate.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType());

            passengerDocument.setPhotoId(fileId);
        }

        passengerDocument = passengerRepository.save(passengerDocument);

        return new PassengerResponseDto(passengerDocument);
    }

    @Override
    public PassengerListResponseDto listAll() {
        return new PassengerListResponseDto(passengerRepository.findAll());
    }

    // TODO: Fazer que esse método retorne um DTO
    @Override
    public Optional<PassengerDocument> update(PassengerRegistration passengerRegistration) throws IOException {
        Optional<PassengerDocument> passengerOptional = passengerRepository.findByEmail(passengerRegistration.getEmail());
        if (passengerOptional.isEmpty()) {
            return passengerOptional;
        }

        PassengerDocument updatedUser = passengerOptional.get();

        if (passengerRegistration.getName() != null)
            updatedUser.setName(passengerRegistration.getName());
        if (passengerRegistration.getEmail() != null)
            updatedUser.setEmail(passengerRegistration.getEmail());
        if (passengerRegistration.getPassword() != null)
            updatedUser.setPassword(passwordEncoder.encode(passengerRegistration.getPassword()));
        if (passengerRegistration.getPhone() != null)
            updatedUser.setPhone(passengerRegistration.getPhone());

        // Atualizar a foto

        if (passengerRegistration.getPhoto() != null && !passengerRegistration.getPhoto().isEmpty()) {
            if (updatedUser.getPhotoId() != null) {
                gridFsTemplate.delete(Query.query(Criteria.where("_id").is(updatedUser.getPhotoId())));
            }
            ObjectId fileId = gridFsTemplate.store(
                    passengerRegistration.getPhoto().getInputStream(),
                    passengerRegistration.getPhoto().getOriginalFilename(),
                    passengerRegistration.getPhoto().getContentType());

            updatedUser.setPhotoId(fileId);
        }

        return Optional.of(passengerRepository.save(updatedUser));
    }

    @Override
    public Boolean delete(String email) {
        Optional<PassengerDocument> passengerOptional = passengerRepository.findByEmail(email);
        if (passengerOptional.isEmpty()) {
            return false;
        } else {
            passengerRepository.deleteById(passengerOptional.get().getId());
            return true;
        }
    }

    @Override
    public PassengerResponseDto findByEmail(String email) {
        Optional<PassengerDocument> passengerDocument = passengerRepository.findByEmail(email);

        if(passengerDocument.isEmpty()){
            return null;
        }

        return new PassengerResponseDto(passengerDocument.get());
    }

    @Override
    public GridFsResource getPhotoResourceByPassengerEmail(String email) {
        Optional<PassengerDocument> passengerOptional = passengerRepository.findByEmail(email);

        if (passengerOptional.isEmpty()) {
            return null;
        }

        PassengerDocument passengerDocument = passengerOptional.get();

        if (passengerDocument.getPhotoId() == null) {
            return null;
        }

        GridFSFile file = gridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(passengerDocument.getPhotoId())));

        if (file == null) {
            return null;
        }

        return gridFsTemplate.getResource(file);
    }

    @Override
    public Boolean getActiveStatus(String email) {
        Optional<PassengerDocument> passengerOptional = passengerRepository.findByEmail(email);

        // TODO: Adicionar erro para caso usuário não seja achado
        if (passengerOptional.isEmpty()) {
            return null;
        }

        PassengerDocument passengerDocument = passengerOptional.get();

        if (passengerDocument.getStatusCadastro() == PassengerDocument.StatusCadastro.ATIVO) {
            return true;
        }

        return false;
    }

    @Override
    public PassengerResponseDto verifyPcdConditions(String email) {
        Optional<PassengerDocument> found = passengerRepository.findByEmail(email);

        if (found.isEmpty()) {
            return null;
        }

        PassengerDocument passengerDocument = found.get();

        // TODO: limitar isso pra PCDs

        for (PcdCondition pcdCondition : passengerDocument.getPcdConditions()) {
            pcdCondition.setValidationStatus(PcdCondition.ValidationStatus.APROVADO);
        }

        passengerDocument.setStatusCadastro(PassengerDocument.StatusCadastro.ATIVO);

        passengerDocument = passengerRepository.save(passengerDocument);

        return new PassengerResponseDto(passengerDocument);
    }

}
