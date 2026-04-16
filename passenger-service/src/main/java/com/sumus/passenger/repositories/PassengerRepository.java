package com.sumus.passenger.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sumus.passenger.domain.entities.PassengerDocument;

import java.util.Optional;
import java.util.List;

@Repository
public interface PassengerRepository extends MongoRepository<PassengerDocument, String> {

    @Override
    List<PassengerDocument> findAll();

    Optional<PassengerDocument> findByEmail(String email);

    Boolean existsByEmail(String email);
}
