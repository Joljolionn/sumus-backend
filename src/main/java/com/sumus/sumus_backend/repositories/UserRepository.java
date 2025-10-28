package com.sumus.sumus_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sumus.sumus_backend.domain.entities.UserDocument;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {

    @Override
    List<UserDocument> findAll();

    Optional<UserDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
