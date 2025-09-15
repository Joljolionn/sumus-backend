
package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sumus.sumus_backend.domain.dtos.AuthResult;
import com.sumus.sumus_backend.domain.dtos.LoginRequest;
import com.sumus.sumus_backend.domain.dtos.UserDto;
import com.sumus.sumus_backend.domain.entities.UserEntity;
import com.sumus.sumus_backend.mappers.impl.UserMapper;
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

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setTelefone(user.getTelefone());
        userEntity.setPassword("encoded");

        when(userMapper.mapFrom(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity created = userService.create(user);

        assertNotNull(created);
        assertEquals("test@example.com", created.getEmail());
        verify(userMapper, times(1)).mapFrom(user);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testListAll() {
        UserEntity user1 = new UserEntity();
        user1.setEmail("teste1@gmail.com");
        UserEntity user2 = new UserEntity();
        user2.setEmail("teste2@gmail.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserEntity> users = userService.listAll();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    
@Test
void testUpdate() {
    UserDto userDto = new UserDto();
    userDto.setEmail("teste@gmail.com");
    userDto.setUsername("novoNome");
    userDto.setTelefone("11 987654321");
    userDto.setPassword(null);

    UserEntity existingUser = new UserEntity();
    existingUser.setId(1L);
    existingUser.setEmail("teste@gmail.com");
    existingUser.setUsername("teste");
    existingUser.setTelefone("11 123456789");
    existingUser.setPassword("123");

    UserEntity savedUser = new UserEntity();
    savedUser.setId(1L);
    savedUser.setEmail("teste@gmail.com");
    savedUser.setUsername("novoNome");
    savedUser.setTelefone("11 987654321");
    savedUser.setPassword("123");

    when(userRepository.findByEmail("teste@gmail.com")).thenReturn(Optional.of(existingUser));

    doAnswer(invocation -> {
        UserEntity entity = invocation.getArgument(0);
        UserDto dto = invocation.getArgument(1);
        entity.setUsername(dto.getUsername());
        entity.setTelefone(dto.getTelefone());
        return null;
    }).when(userMapper).updateEntityFromDto(any(UserEntity.class), any(UserDto.class));

    when(userRepository.save(existingUser)).thenReturn(savedUser);

    Optional<UserEntity> result = userService.update(userDto);

    assertTrue(result.isPresent());
    assertEquals("novoNome", result.get().getUsername());
    assertEquals("11 987654321", result.get().getTelefone());

    verify(userRepository, times(1)).findByEmail("teste@gmail.com");
    verify(userMapper, times(1)).updateEntityFromDto(existingUser, userDto);
    verify(userRepository, times(1)).save(existingUser);
}


    @Test
    void testDelete_UserExists() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("toDelete@example.com");

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
        user.setPassword("encoded");

        when(userRepository.findByEmail("teste@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "encoded")).thenReturn(true);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@gmail.com");
        loginRequest.setPassword("123");

        AuthResult authResult = userService.login(loginRequest);

        assertNotNull(authResult);
        assertEquals(AuthResult.Status.SUCCESS, authResult.getStatus());
        assertNotNull(authResult.getToken());
        verify(userRepository, times(1)).findByEmail("teste@gmail.com");
        verify(passwordEncoder, times(1)).matches("123", "encoded");
    }
}

