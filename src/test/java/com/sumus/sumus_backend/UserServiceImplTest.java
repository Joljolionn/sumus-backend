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
import com.sumus.sumus_backend.domain.entities.UserDocuments;
import com.sumus.sumus_backend.mappers.impl.UserMapper;
import com.sumus.sumus_backend.repositories.UserRepository;
import com.sumus.sumus_backend.services.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockMultipartFile;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository; // 1. Mock do repositório de usuários

    @Mock
    private PasswordEncoder passwordEncoder; // 2. Mock do encoder de senhas

    @Mock
    private UserMapper userMapper; // 3. Mock do mapper entre DTO e entidade

    @InjectMocks
    private UserServiceImpl userService; // 4. Serviço sendo testado

    @BeforeEach
    void setUp() {
        // 5. Inicializa os mocks antes de cada teste
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws IOException { 
        // 1. Prepara o DTO de usuário com dados básicos
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");

        // 2. Cria uma foto mockada para o upload
        MockMultipartFile mockFoto = new MockMultipartFile(
            "foto",
            "foto_teste.jpg",
            "image/jpeg",
            "dados_da_foto".getBytes()
        );

        // 3. Prepara a entidade esperada após mapeamento
        UserDocuments userEntity = new UserDocuments();
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setTelefone(user.getTelefone());
        userEntity.setPassword("encoded");
        userEntity.setContentType("image/jpeg");
        userEntity.setFoto("dados_da_foto".getBytes());

        // 4. Configura os mocks para simular o comportamento do mapper e do repositório
        when(userMapper.mapFrom(user)).thenReturn(userEntity); // quando mapear DTO -> entidade
        when(userRepository.save(userEntity)).thenReturn(userEntity); // quando salvar a entidade, retorna ela mesma

        // 5. Executa o método create
        UserDocuments created = userService.create(user);

        // 6. Verifica se o resultado está correto
        assertNotNull(created);
        assertEquals("test@example.com", created.getEmail());
        assertArrayEquals("dados_da_foto".getBytes(), created.getFoto());
        assertEquals("image/jpeg", created.getContentType());

        // 7. Confirma que os mocks foram chamados corretamente
        verify(userMapper, times(1)).mapFrom(user);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testCreate_IOException() throws IOException { 
        // Teste para exceção durante mapeamento de foto

        // 1. Prepara o DTO de usuário
        UserDto user = new UserDto();
        user.setEmail("test@example.com");
        user.setUsername("teste");
        user.setPassword("123");
        user.setTelefone("11 123456789");
        user.setFoto(new MockMultipartFile("foto", "test.jpg", "image/jpeg", new byte[0]));

        // 2. Configura o mapper para lançar IOException ao mapear o DTO
        when(userMapper.mapFrom(any(UserDto.class))).thenThrow(new IOException("Erro ao ler foto"));

        // 3. Verifica se a exceção é lançada
        assertThrows(IOException.class, () -> userService.create(user));

        // 4. Verifica que o save do repositório nunca foi chamado
        verify(userRepository, never()).save(any(UserDocuments.class));
    }

    @Test
    void testListAll() {
        // Teste de listagem de todos os usuários

        // 1. Prepara usuários simulados
        UserDocuments user1 = new UserDocuments();
        user1.setEmail("teste1@gmail.com");
        UserDocuments user2 = new UserDocuments();
        user2.setEmail("teste2@gmail.com");

        // 2. Configura o repositório para retornar esses usuários
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // 3. Executa o método listAll
        List<UserDocuments> users = userService.listAll();

        // 4. Verifica se todos os usuários foram retornados
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() throws IOException { 
        // Teste de atualização de usuário, incluindo alteração de foto

        // 1. Prepara o DTO de atualização
        UserDto userDto = new UserDto();
        userDto.setEmail("teste@gmail.com");
        userDto.setUsername("novoNome");
        userDto.setTelefone("11 987654321");
        userDto.setPassword(null);

        // 2. Adiciona foto mock para atualização
        MockMultipartFile mockFoto = new MockMultipartFile(
            "foto",
            "foto_nova.png",
            "image/png",
            "novos_dados".getBytes()
        );
        userDto.setFoto(mockFoto);

        // 3. Prepara o usuário existente antes da atualização
        UserDocuments existingUser = new UserDocuments();
        existingUser.setEmail("teste@gmail.com");
        existingUser.setUsername("teste");
        existingUser.setTelefone("11 123456789");
        existingUser.setPassword("123");

        // 4. Define como o usuário atualizado deverá ficar
        UserDocuments savedUser = new UserDocuments();
        savedUser.setEmail("teste@gmail.com");
        savedUser.setUsername("novoNome");
        savedUser.setTelefone("11 987654321");
        savedUser.setPassword("123");
        savedUser.setContentType("image/png");
        savedUser.setFoto("novos_dados".getBytes());

        // 5. Configura o repositório para encontrar o usuário pelo email
        when(userRepository.findByEmail("teste@gmail.com")).thenReturn(Optional.of(existingUser));

        // 6. Configura o mapper para atualizar a entidade com base no DTO
        doAnswer(invocation -> {
            UserDocuments entity = invocation.getArgument(0);
            UserDto dto = invocation.getArgument(1);
            entity.setUsername(dto.getUsername());
            entity.setTelefone(dto.getTelefone());
            if(dto.getFoto() != null){
                entity.setContentType(dto.getFoto().getContentType());
                entity.setFoto(dto.getFoto().getBytes());
            }
            return null;
        }).when(userMapper).updateEntityFromDto(any(UserDocuments.class), any(UserDto.class));

        // 7. Configura o repositório para salvar a entidade atualizada
        when(userRepository.save(existingUser)).thenReturn(savedUser);

        // 8. Executa o método update
        Optional<UserDocuments> result = userService.update(userDto);

        // 9. Verifica se a atualização foi correta
        assertTrue(result.isPresent());
        assertEquals("novoNome", result.get().getUsername());
        assertEquals("11 987654321", result.get().getTelefone());
        assertArrayEquals("novos_dados".getBytes(), result.get().getFoto());
        assertEquals("image/png", result.get().getContentType());

        // 10. Confirma as interações com mocks
        verify(userRepository, times(1)).findByEmail("teste@gmail.com");
        verify(userMapper, times(1)).updateEntityFromDto(existingUser, userDto);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDelete_UserExists() {
        // Teste de exclusão de usuário existente

        // 1. Prepara usuário a ser deletado
        UserDocuments user = new UserDocuments();
        user.setEmail("toDelete@example.com");

        // 2. Configura o repositório para encontrar o usuário
        when(userRepository.findByEmail("toDelete@example.com")).thenReturn(Optional.of(user));

        // 3. Configura o repositório para não fazer nada ao deletar (simula sucesso)
        doNothing().when(userRepository).deleteById(user.getId());

        // 4. Executa o método delete
        Boolean result = userService.delete("toDelete@example.com");

        // 5. Verifica se o retorno indica sucesso
        assertTrue(result);

        // 6. Confirma interações com mocks
        verify(userRepository, times(1)).findByEmail("toDelete@example.com");
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void testDelete_UserNotExists() {
        // Teste de exclusão de usuário inexistente

        // 1. Configura o repositório para não encontrar o usuário
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // 2. Executa o método delete
        Boolean result = userService.delete("notfound@example.com");

        // 3. Verifica que o retorno indica falha
        assertFalse(result);

        // 4. Confirma interações com mocks
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void testFindByEmail() {
        // Teste de busca de usuário por email

        // 1. Prepara usuário simulado
        UserDocuments user = new UserDocuments();
        user.setEmail("findme@example.com");

        // 2. Configura o repositório para retornar o usuário
        when(userRepository.findByEmail("findme@example.com")).thenReturn(Optional.of(user));

        // 3. Executa o método findByEmail
        Optional<UserDocuments> found = userService.findByEmail("findme@example.com");

        // 4. Verifica se o usuário foi encontrado
        assertTrue(found.isPresent());
        assertEquals("findme@example.com", found.get().getEmail());

        // 5. Confirma interações com o mock
        verify(userRepository, times(1)).findByEmail("findme@example.com");
    }

    @Test
    void testLogin() {
        // Teste de login com email e senha corretos

        // 1. Prepara usuário simulado com senha codificada
        UserDocuments user = new UserDocuments();
        user.setEmail("teste@gmail.com");
        user.setPassword("encoded");

        // 2. Configura o repositório para retornar o usuário ao buscar pelo email
        when(userRepository.findByEmail("teste@gmail.com")).thenReturn(Optional.of(user));

        // 3. Configura o encoder para validar a senha
        when(passwordEncoder.matches("123", "encoded")).thenReturn(true);

        // 4. Prepara DTO de login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@gmail.com");
        loginRequest.setPassword("123");

        // 5. Executa o método login
        AuthResult authResult = userService.login(loginRequest);

        // 6. Verifica se o login foi bem-sucedido
        assertNotNull(authResult);
        assertEquals(AuthResult.Status.SUCCESS, authResult.getStatus());
        assertNotNull(authResult.getToken());

        // 7. Confirma interações com mocks
        verify(userRepository, times(1)).findByEmail("teste@gmail.com");
        verify(passwordEncoder, times(1)).matches("123", "encoded");
    }
}

