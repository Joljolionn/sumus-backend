package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.services.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setId(1L);
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");


        when(userRepository.save(user)).thenReturn(user);

        UserEntity created = userService.create(user);

        assertNotNull(created);
        assertEquals("test@example.com", created.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testListAll() {
        UserEntity user1 = new UserEntity();
        user1.setEmail("teste@gmail.com");
        user1.setUsername("teste");
        user1.setPassword("123");
        user1.setTelefone("11 123456789");

        UserEntity user2 = new UserEntity();
        user2.setEmail("teste@gmail.com");
        user2.setUsername("teste");
        user2.setPassword("123");
        user2.setTelefone("11 123456789");


        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserEntity> users = userService.listAll();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UserEntity user = new UserEntity();
        user.setEmail("teste@gmail.com");
        user.setId(1L);
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        when(userRepository.save(user)).thenReturn(user);

        UserEntity updated = user;
        updated.setEmail("updated@example.com");

        updated = userService.update(user);

        assertEquals("updated@example.com", updated.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDelete_UserExists() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("toDelete@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        when(userRepository.findByEmail("toDelete@example.com")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        Boolean result = userService.delete("toDelete@example.com");

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail("toDelete@example.com");
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_UserNotExists() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Boolean result = userService.delete("notfound@example.com");

        assertFalse(result);
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("findme@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        when(userRepository.findByEmail("findme@example.com")).thenReturn(Optional.of(user));

        Optional<UserEntity> found = userService.findByEmail("findme@example.com");

        assertTrue(found.isPresent());
        assertEquals("findme@example.com", found.get().getEmail());
        verify(userRepository, times(1)).findByEmail("findme@example.com");
    }

    @Test
    void testLogin() {
        UserEntity user = new UserEntity();
        user.setEmail("teste@gmail.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        when(userRepository.findByEmail("teste@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", user.getPassword())).thenReturn(true);

        AuthResult authResult = userService.login("teste@gmail.com", "123");

        assertNotNull(authResult);
        assertFalse(authResult.getToken().isEmpty());
        assertEquals(AuthResult.Status.SUCCESS, authResult.getStatus());
        verify(userRepository, times(1)).findByEmail("teste@gmail.com");
    }
}
