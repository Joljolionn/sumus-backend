package com.sumus.driver.services.impl;

import java.io.IOException;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sumus.driver.domain.dtos.request.DriverRegistrationRequest;
import com.sumus.driver.domain.dtos.request.DriverUpdateRequest;
import com.sumus.driver.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.driver.domain.dtos.response.DriverListResponseDto;
import com.sumus.driver.domain.dtos.response.DriverResponseDto;
import com.sumus.driver.domain.entities.DriverDocument;
import com.sumus.driver.exceptions.BusinessRuleException;
import com.sumus.driver.exceptions.ResourceNotFoundException;
import com.sumus.driver.repositories.DriverRepository;
import com.sumus.driver.services.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private GridFsTemplate gridFsTemplate;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public DriverResponseDto create(DriverRegistrationRequest driverRegistration) throws IOException {

    if (driverRepository.existsByEmail(driverRegistration.email())) {
      throw new BusinessRuleException(
          "Erro: O e-mail " + driverRegistration.email() + " já está cadastrado no sistema.");
    }

    DriverDocument driverDocument = new DriverDocument(driverRegistration.name(),
        driverRegistration.email(), passwordEncoder.encode(driverRegistration.password()),
        driverRegistration.phone(), driverRegistration.cnh());

    driverDocument = driverRepository.save(driverDocument);

    return new DriverResponseDto(driverDocument);
  }

  @Override
  public DriverListResponseDto listAll() {
    List<DriverDocument> driverDocuments = driverRepository.findAll();

    return new DriverListResponseDto(
        driverDocuments.stream().map(d -> new DriverResponseDto(d)).toList());
  }

  @Override
  public GridFsResource getPhotoResourceByDriverEmail(String email) {
    DriverDocument driverDocument = driverRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Motorista não encontrado com o e-mail: " + email));

    if (driverDocument.getPhotoId() == null) {
      throw new ResourceNotFoundException("O motorista " + email + " não possui foto cadastrada.");
    }

    GridFSFile file =
        gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(driverDocument.getPhotoId())));

    if (file == null) {
      throw new ResourceNotFoundException("Arquivo de foto não encontrado no banco de dados.");
    }

    return gridFsTemplate.getResource(file);
  }

  @Override
  public DriverResponseDto findByEmail(String email) {
    DriverDocument driverDocument = driverRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Motorista não encontrado com o e-mail: " + email));

    return new DriverResponseDto(driverDocument);
  }

  @Override
  public DriverResponseDto verifyDocuments(String email) {
    DriverDocument driverDocument = driverRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Motorista não encontrado com o e-mail: " + email));

    driverDocument.setIsActive(true);
    driverDocument = driverRepository.save(driverDocument);

    return new DriverResponseDto(driverDocument);
  }

  @Override
  public DriverResponseDto update(String email, DriverUpdateRequest driverUpdateRequest)
      throws IOException {

    DriverDocument driverDocument = driverRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Motorista não encontrado com o e-mail: " + email));

    if (driverUpdateRequest.name() != null)
      driverDocument.setName(driverUpdateRequest.name());
    if (driverUpdateRequest.email() != null)
      driverDocument.setEmail(driverUpdateRequest.email());
    if (driverUpdateRequest.phone() != null)
      driverDocument.setPhone(driverUpdateRequest.phone());
    if (driverUpdateRequest.cnh() != null)
      driverDocument.setCnh(driverUpdateRequest.cnh());

    if (driverUpdateRequest.photo() != null && !driverUpdateRequest.photo().isEmpty()) {
      if (driverDocument.getPhotoId() != null) {
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(driverDocument.getPhotoId())));
      }
      ObjectId fileId = gridFsTemplate.store(driverUpdateRequest.photo().getInputStream(),
          driverUpdateRequest.photo().getOriginalFilename(),
          driverUpdateRequest.photo().getContentType());

      driverDocument.setPhotoId(fileId);
    }

    return new DriverResponseDto(driverRepository.save(driverDocument));
  }

  @Override
  public Boolean updatePassword(String email, PasswordUpdateRequest passwordUpdateRequest) {
    DriverDocument driverDocument = driverRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Motorista não encontrado com o e-mail: " + email));

    driverDocument.setPassword(passwordEncoder.encode(passwordUpdateRequest.password()));
    driverRepository.save(driverDocument);

    return true;
  }

  @Override
  public Boolean deleteDriver(String email) {
    DriverDocument driverDocument = driverRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Motorista não encontrado com o e-mail: " + email));

    driverRepository.deleteById(driverDocument.getId());
    return true;
  }

}
