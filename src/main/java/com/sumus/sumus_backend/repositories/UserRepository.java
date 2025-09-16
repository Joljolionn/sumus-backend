package com.sumus.sumus_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sumus.sumus_backend.domain.entities.UserEntity;


// Interface para fazer uso de metodos crús disponibilizadas pelo JPA para
// interagir com o banco de dados usando a classe de Entidade e o seu ID (Long)
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    @Override
    List<UserEntity> findAll();

    Optional<UserEntity> findByEmail(String email);
}
