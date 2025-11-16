package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;
import com.sumus.sumus_backend.repositories.passenger.PassengerRepository;
import com.sumus.sumus_backend.utils.TestPassengerEntities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
@Import(TestPassengerEntities.class)
public class UserRepositoryIntegrationTests {

    @Autowired
    private PassengerRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestPassengerEntities testEntities;


    /**
     * Limpa a coleção de usuários após a execução de cada método de teste.
     */
    @BeforeEach
    void tearDown() {
        // Deleta todos os documentos da coleção UserDocument
        mongoTemplate.dropCollection(PassengerDocument.class);
    }

    @Test
    void testSaveAndFindByEmail() {
        PassengerDocument user = testEntities.entityOne();

        userRepository.save(user);

        Optional<PassengerDocument> found = userRepository.findByEmail(user.getEmail());

        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
        assertEquals(user.getName(), found.get().getName());
    }

    @Test
    void testFindAll() {
        PassengerDocument userEntity1 = testEntities.entityOne();
        PassengerDocument userEntity2 = testEntities.entityTwo();

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        List<PassengerDocument> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

}
