package com.sumus.sumus_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sumus.sumus_backend.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    @Override
    List<UserEntity> findAll();

    Optional<UserEntity> findByEmail(String email);
}
