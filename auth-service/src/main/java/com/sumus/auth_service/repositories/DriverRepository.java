package com.sumus.auth_service.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sumus.auth_service.domain.documents.DriverAuthDocument;

@Repository
public interface DriverRepository extends MongoRepository<DriverAuthDocument, String> {

  Optional<DriverAuthDocument> findByEmail(String email);

}
