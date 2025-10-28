package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.entities.UserDocument;
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
        // Deleta todos os documentos da coleção UserDocument
        mongoTemplate.dropCollection(UserDocument.class);
    }

    @Test
    void testSaveAndFindByEmail() {
        UserDocument user = testEntities.entityOne();

        userRepository.save(user);

        Optional<UserDocument> found = userRepository.findByEmail(user.getEmail());

        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
        assertEquals(user.getName(), found.get().getName());
    }

    @Test
    void testFindAll() {
        UserDocument userEntity1 = testEntities.entityOne();
        UserDocument userEntity2 = testEntities.entityTwo();

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        List<UserDocument> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

}
