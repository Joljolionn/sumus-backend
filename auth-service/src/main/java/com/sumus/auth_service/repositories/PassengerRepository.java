package com.sumus.auth_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sumus.auth_service.domain.documents.PassengerAuthDocument;
import java.util.Optional;

@Repository
public interface PassengerRepository extends MongoRepository<PassengerAuthDocument, String> {

  Optional<PassengerAuthDocument> findByEmail(String email);

}
