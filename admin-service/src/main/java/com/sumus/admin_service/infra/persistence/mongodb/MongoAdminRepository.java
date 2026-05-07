package com.sumus.admin_service.infra.persistence.mongodb;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.sumus.admin_service.infra.persistence.mongodb.documents.AdminDocument;

interface MongoAdminRepository extends MongoRepository<AdminDocument, String> {

  Optional<AdminDocument> findByEmail(String email);

  boolean existsByEmail(String email);

}
