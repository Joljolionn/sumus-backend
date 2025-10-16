package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.sumus.sumus_backend.domain.entities.UserDocuments;
import com.sumus.sumus_backend.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
public class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


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
        UserDocuments user = new UserDocuments();
        user.setEmail("integration@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        userRepository.save(user);

        Optional<UserDocuments> found = userRepository.findByEmail("integration@example.com");

        assertTrue(found.isPresent());
        assertEquals("integration@example.com", found.get().getEmail());
    }

    @Test
    void testFindAll() {
        UserDocuments userEntity1 = new UserDocuments();
        userEntity1.setEmail("user1@example.com");
        userEntity1.setUsername("teste");
        userEntity1.setPassword("123");
        userEntity1.setTelefone("11 123456789");
        UserDocuments userEntity2 = new UserDocuments();
        userEntity2.setEmail("user2@example.com");
        userEntity2.setPassword("123");
        userEntity2.setUsername("teste");
        userEntity2.setPassword("123");
        userEntity2.setTelefone("11 123456789");

        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        var users = userRepository.findAll();

        assertEquals(2, users.size());
    }
}
