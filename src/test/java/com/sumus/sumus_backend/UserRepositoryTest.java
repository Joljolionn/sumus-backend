package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.sumus.sumus_backend.entities.UserEntity;
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

        userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByEmail("integration@example.com");

        assertTrue(found.isPresent());
        assertEquals("integration@example.com", found.get().getEmail());
    }

    @Test
    void testFindAll() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("user1@example.com");        
        UserEntity userEntity2 = new UserEntity();
        userEntity1.setEmail("user2@example.com");
        userRepository.save(userEntity1);
        userRepository.save(userEntity2);

        var users = userRepository.findAll();

        assertEquals(2, users.size());
    }
}
