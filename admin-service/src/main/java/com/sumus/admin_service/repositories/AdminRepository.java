package com.sumus.admin_service.repositories;

import java.util.List;
import java.util.Optional;
import com.sumus.admin_service.domain.models.Admin;

public interface AdminRepository {

  List<Admin> findAll();

  Optional<Admin> findByEmail(String email);

  boolean existsByEmail(String email);

  Admin save(Admin admin);

  void deleteById(String id);
  
}
