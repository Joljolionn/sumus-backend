package com.sumus.admin_service.infra.persistence.mongodb;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sumus.admin_service.domain.models.Admin;
import com.sumus.admin_service.infra.persistence.mongodb.documents.AdminDocument;
import com.sumus.admin_service.repositories.AdminRepository;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

  @Autowired
  private MongoAdminRepository mongoRepository;

  @Override
  public List<Admin> findAll() {
    return mongoRepository.findAll().stream().map(document -> document.toDomain()).toList();
  }

  @Override
  public Optional<Admin> findByEmail(String email) {
    return mongoRepository.findByEmail(email).map(document -> document.toDomain());
  }

  @Override
  public boolean existsByEmail(String email) {
    return mongoRepository.existsByEmail(email);
  }

  @Override
  public Admin save(Admin admin) {
    return mongoRepository.save(AdminDocument.fromDomain(admin)).toDomain();
  }

  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }
    
  
}
