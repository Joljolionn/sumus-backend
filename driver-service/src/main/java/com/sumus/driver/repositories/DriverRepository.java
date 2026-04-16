package com.sumus.driver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sumus.driver.domain.entities.DriverDocument;


@Repository
public interface DriverRepository extends MongoRepository<DriverDocument, String> {

    @Override
    List<DriverDocument> findAll();

    Optional<DriverDocument> findByEmail(String email);

    Boolean existsByEmail(String email);

}
