package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.sumus.sumus_backend.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("integration@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByEmail("integration@example.com");

        assertTrue(found.isPresent());
        assertEquals("integration@example.com", found.get().getEmail());
    }

    @Test
    void testFindAll() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("user1@example.com");
        userEntity1.setUsername("teste");
        userEntity1.setPassword("123");
        userEntity1.setTelefone("11 123456789");
        UserEntity userEntity2 = new UserEntity();
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
