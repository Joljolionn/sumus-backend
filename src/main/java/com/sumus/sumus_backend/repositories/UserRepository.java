package com.sumus.sumus_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sumus.sumus_backend.domain.entities.UserDocuments;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserDocuments, String> {

    @Override
    List<UserDocuments> findAll();

    Optional<UserDocuments> findByEmail(String email);

    boolean existsByEmail(String email);
}