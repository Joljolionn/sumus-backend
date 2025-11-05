package com.sumus.sumus_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sumus.sumus_backend.domain.entities.PassengerDocument;

import java.util.Optional;
import java.util.List;

@Repository
public interface PassengerRepository extends MongoRepository<PassengerDocument, String> {

    @Override
    List<PassengerDocument> findAll();

    Optional<PassengerDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
