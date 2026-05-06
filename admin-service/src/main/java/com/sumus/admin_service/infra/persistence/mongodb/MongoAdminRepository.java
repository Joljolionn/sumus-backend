package com.sumus.admin_service.infra.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAdminRepository extends MongoRepository<AdminDocument, String> {

}
