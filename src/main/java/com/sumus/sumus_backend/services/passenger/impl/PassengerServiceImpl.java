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

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistrationRequest;
import com.sumus.sumus_backend.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
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
    public PassengerResponseDto create(PassengerRegistrationRequest passengerRegistration) throws IOException {

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

        passengerDocument = passengerRepository.save(passengerDocument);

        return new PassengerResponseDto(passengerDocument);
    }

    @Override
    public PassengerListResponseDto listAll() {
        return new PassengerListResponseDto(passengerRepository.findAll());
    }

    @Override
    public PassengerResponseDto update(String email, PassengerUpdateRequest passengerUpdateRequest) throws IOException {
        Optional<PassengerDocument> passengerOptional = passengerRepository
                .findByEmail(email);
        if (passengerOptional.isEmpty()) {
            return null;
        }

        PassengerDocument passengerDocument = passengerOptional.get();

        if (passengerUpdateRequest.getName() != null)
            passengerDocument.setName(passengerUpdateRequest.getName());
        if (passengerUpdateRequest.getEmail() != null)
            passengerDocument.setEmail(passengerUpdateRequest.getEmail());
        if (passengerUpdateRequest.getPhone() != null)
            passengerDocument.setPhone(passengerUpdateRequest.getPhone());

        // Atualizar a foto
        if (passengerUpdateRequest.getPhoto() != null && !passengerUpdateRequest.getPhoto().isEmpty()) {
            if (passengerDocument.getPhotoId() != null) {
                gridFsTemplate.delete(Query.query(Criteria.where("_id").is(passengerDocument.getPhotoId())));
            }
            ObjectId fileId = gridFsTemplate.store(
                    passengerUpdateRequest.getPhoto().getInputStream(),
                    passengerUpdateRequest.getPhoto().getOriginalFilename(),
                    passengerUpdateRequest.getPhoto().getContentType());

            passengerDocument.setPhotoId(fileId);
        }

        return new PassengerResponseDto(passengerRepository.save(passengerDocument));
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

        if (passengerDocument.isEmpty()) {
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

    @Override
    public Boolean updatePassword(String email, PasswordUpdateRequest passwordUpdateRequest) {
        Optional<PassengerDocument> passengerOptional = passengerRepository.findByEmail(email);

        if (passengerOptional.isEmpty()) {
            return false;
        }

        PassengerDocument passengerDocument = passengerOptional.get();

        passengerDocument.setPassword(passwordEncoder.encode(passwordUpdateRequest.getPassword()));

        passengerRepository.save(passengerDocument);

        return true;
    }

}
