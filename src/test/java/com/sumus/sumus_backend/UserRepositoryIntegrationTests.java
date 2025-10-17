package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.entities.UserDocuments;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.utils.TestEntities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
@Import(TestEntities.class)
public class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestEntities testEntities;


    /**
     * Limpa a coleção de usuários após a execução de cada método de teste.
     */
    @BeforeEach
    void tearDown() {
        // Deleta todos os documentos da coleção UserDocuments
        mongoTemplate.dropCollection(UserDocuments.class);
    }

    @Test
    void testSaveAndFindByEmail() {
        UserDocuments user = testEntities.entityOne();

        userRepository.save(user);

        Optional<UserDocuments> found = userRepository.findByEmail(user.getEmail());

        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
        assertEquals(user.getUsername(), found.get().getUsername());
    }

    @Test
    void testFindAll() {
        UserDocuments userEntity1 = testEntities.entityOne();
        UserDocuments userEntity2 = testEntities.entityTwo();

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        List<UserDocuments> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

}
