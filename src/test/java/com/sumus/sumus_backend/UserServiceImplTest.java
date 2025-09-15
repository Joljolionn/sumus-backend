package com.sumus.sumus_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile; // Importe esta classe

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
    void testCreate_Success() throws IOException { // Adicione 'throws IOException' para simplificar o teste
        // 1. Prepara o UserDto com uma foto mockada
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");
        
        // Cria um Mock MultipartFile
        MockMultipartFile mockFoto = new MockMultipartFile(
            "foto",
            "foto_teste.jpg",
            "image/jpeg",
            "dados_da_foto".getBytes()
        );
        user.setFoto(mockFoto);

        // 2. Prepara a UserEntity esperada
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setTelefone(user.getTelefone());
        userEntity.setPassword("encoded");
        userEntity.setContentType("image/jpeg");
        userEntity.setFoto("dados_da_foto".getBytes());

        // 3. Configura os Mocks
        when(userMapper.mapFrom(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // 4. Executa o método a ser testado
        UserEntity created = userService.create(user);

        // 5. Faz as verificações (asserts)
        assertNotNull(created);
        assertEquals("test@example.com", created.getEmail());
        assertArrayEquals("dados_da_foto".getBytes(), created.getFoto());
        assertEquals("image/jpeg", created.getContentType());
        
        // 6. Verifica as interações com os mocks
        verify(userMapper, times(1)).mapFrom(user);
        verify(userRepository, times(1)).save(userEntity);
    }
    
    @Test
    void testCreate_IOException() throws IOException { // Teste para a exceção
        // 1. Prepara o DTO
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");
        user.setFoto(new MockMultipartFile("foto", "test.jpg", "image/jpeg", new byte[0]));

        // 2. Configura o mock para lançar IOException
        when(userMapper.mapFrom(any(UserDto.class))).thenThrow(new IOException("Erro ao ler foto"));
        
        // 3. Espera a exceção ser lançada
        assertThrows(IOException.class, () -> userService.create(user));
        
        // 4. Verifica que o save não foi chamado
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // ... (o resto do seu código de teste, que parece estar correto, pode ficar aqui)
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
void testUpdate() throws IOException { // Adicione 'throws IOException'
    UserDto userDto = new UserDto();
    userDto.setEmail("teste@gmail.com");
    userDto.setUsername("novoNome");
    userDto.setTelefone("11 987654321");
    userDto.setPassword(null);
    
    // Adicione uma foto mock para o DTO de atualização
    MockMultipartFile mockFoto = new MockMultipartFile(
            "foto",
            "foto_nova.png",
            "image/png",
            "novos_dados".getBytes()
    );
    userDto.setFoto(mockFoto);

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
    savedUser.setContentType("image/png");
    savedUser.setFoto("novos_dados".getBytes());

    when(userRepository.findByEmail("teste@gmail.com")).thenReturn(Optional.of(existingUser));

    // Correção: Mocar o comportamento de updateEntityFromDto para incluir a foto
    doAnswer(invocation -> {
        UserEntity entity = invocation.getArgument(0);
        UserDto dto = invocation.getArgument(1);
        entity.setUsername(dto.getUsername());
        entity.setTelefone(dto.getTelefone());
        if(dto.getFoto() != null){
            entity.setContentType(dto.getFoto().getContentType());
            entity.setFoto(dto.getFoto().getBytes());
        }
        return null;
    }).when(userMapper).updateEntityFromDto(any(UserEntity.class), any(UserDto.class));
    
    when(userRepository.save(existingUser)).thenReturn(savedUser);

    Optional<UserEntity> result = userService.update(userDto);

    assertTrue(result.isPresent());
    assertEquals("novoNome", result.get().getUsername());
    assertEquals("11 987654321", result.get().getTelefone());
    assertArrayEquals("novos_dados".getBytes(), result.get().getFoto());
    assertEquals("image/png", result.get().getContentType());

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