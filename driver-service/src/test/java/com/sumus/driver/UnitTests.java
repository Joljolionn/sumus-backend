package com.sumus.driver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.sumus.driver.domain.dtos.request.DriverRegistrationRequest;
import com.sumus.driver.domain.dtos.request.DriverUpdateRequest;
import com.sumus.driver.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.driver.domain.dtos.response.DriverListResponseDto;
import com.sumus.driver.domain.dtos.response.DriverResponseDto;
import com.sumus.driver.domain.entities.DriverDocument;
import com.sumus.driver.exceptions.BusinessRuleException;
import com.sumus.driver.exceptions.ResourceNotFoundException;
import com.sumus.driver.repositories.DriverRepository;
import com.sumus.driver.services.impl.DriverServiceImpl;
import com.sumus.driver.utils.TestDriverDocuments;
import com.sumus.driver.utils.TestDriverRegistrationRequests;

@ExtendWith(MockitoExtension.class)
class UnitTests {

  @Mock
  private DriverRepository driverRepository;

  @Mock
  private GridFsTemplate gridFsTemplate;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private DriverServiceImpl driverService;


  private TestDriverDocuments testDriverDocuments;
  private TestDriverRegistrationRequests testDriverRegistrationRequests;
  private DriverDocument driverDocument;
  private String email;

  @BeforeEach
  void setUp() {
    testDriverDocuments = new TestDriverDocuments();
    testDriverRegistrationRequests = new TestDriverRegistrationRequests();
    driverDocument = testDriverDocuments.entityOne();
    email = driverDocument.getEmail();
  }

  @Test
  @DisplayName("Deve criar um motorista com sucesso")
  void create_ShouldReturnResponseDto_WhenSuccess() throws IOException {
    DriverRegistrationRequest request = testDriverRegistrationRequests.dtoOne();

    when(driverRepository.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(driverRepository.save(any(DriverDocument.class))).thenReturn(driverDocument);

    DriverResponseDto result = driverService.create(request);

    assertNotNull(result, "A resposta não deve ser nula");
    assertEquals(email, result.email(),
        "O email da resposta retornada deve ser igual ao email enviado");
    verify(driverRepository, times(1)).save(any(DriverDocument.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao criar motorista com e-mail já existente")
  void create_ShouldThrowException_WhenEmailExists() {
    DriverRegistrationRequest request = testDriverRegistrationRequests.dtoOne();
    when(driverRepository.existsByEmail(email)).thenReturn(true);

    assertThrows(BusinessRuleException.class, () -> {
      driverService.create(request);
    }, "A chamada deve retornar uma exceção devido a já haver um usuário cadastrado com esse email");

    verify(driverRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve retornar corretamente uma lista com todos os motoristas cadastrados")
  void listAll_ShoudReturnDriverList() {
    List<DriverDocument> mockList = new ArrayList<>();
    mockList.addLast(testDriverDocuments.entityOne());
    mockList.addLast(testDriverDocuments.entityTwo());
    mockList.addLast(testDriverDocuments.entityThree());
    when(driverRepository.findAll()).thenReturn(mockList);

    DriverListResponseDto result = driverService.listAll();

    assertNotNull(result, "A resposta não pode ser nula");
    assertEquals(3, result.drivers().size(), "A quantidade de motoristas deve ser 3");

    assertEquals(mockList.get(0).getEmail(), result.drivers().get(0).email());
    assertEquals(mockList.get(0).getName(), result.drivers().get(0).name());

    List<String> emailsEsperados = mockList.stream().map(DriverDocument::getEmail).toList();
    List<String> emailsRetornados =
        result.drivers().stream().map(DriverResponseDto::email).toList();

    assertEquals(emailsEsperados, emailsRetornados,
        "Todos os emails devem coincidir na ordem correta");
  }

  @Test
  @DisplayName("Deve ser capaz de atualizar por completo o motorista cadastrado")
  void update_ShouldUpdateAllFields() throws IOException {
    DriverDocument originalEntity = testDriverDocuments.entityOne();
    originalEntity.setPhotoId(new ObjectId());

    DriverDocument updatedData = testDriverDocuments.entityTwo();
    MockMultipartFile newPhoto =
        new MockMultipartFile("photo", "new.jpg", "image/jpeg", "content".getBytes());

    DriverUpdateRequest updateRequest = new DriverUpdateRequest(updatedData.getName(),
        updatedData.getEmail(), updatedData.getPhone(), updatedData.getCnh(), newPhoto);

    ObjectId newPhotoId = new ObjectId();

    when(driverRepository.findByEmail(email)).thenReturn(Optional.of(originalEntity));
    when(gridFsTemplate.store(any(InputStream.class), any(String.class), any(String.class)))
        .thenReturn(newPhotoId);
    when(driverRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    DriverResponseDto result = driverService.update(email, updateRequest);

    assertNotNull(result, "A resposta retornado não deve ser nula");

    assertEquals(updatedData.getName(), result.name(), "O nome deve ser atualizado");

    assertEquals(updatedData.getEmail(), result.email(), "O email deve ser atualizado");

    assertEquals(newPhotoId, originalEntity.getPhotoId(), "O ID da foto deve ser atualizado");


    verify(gridFsTemplate, times(1)).delete(any(Query.class));
    verify(gridFsTemplate, times(1)).store(any(InputStream.class), any(String.class),
        any(String.class));
    verify(driverRepository).save(originalEntity);
  }

  @Test
  @DisplayName("Deve ser capaz de atualizar somente um atributo do motorista")
  void update_ShouldUpdateOnlyName() throws IOException {
    DriverDocument originalEntity = testDriverDocuments.entityThree();
    String originalEmail = originalEntity.getEmail();
    String originalPhone = originalEntity.getPhone();
    ObjectId originalPhotoId = new ObjectId();
    originalEntity.setPhotoId(originalPhotoId);

    String newName = testDriverDocuments.entitySix().getName();
    DriverUpdateRequest updateRequest = new DriverUpdateRequest(newName, null, null, null, null);

    when(driverRepository.findByEmail(originalEmail)).thenReturn(Optional.of(originalEntity));
    when(driverRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    DriverResponseDto result = driverService.update(originalEmail, updateRequest);

    assertEquals(newName, result.name(), "O nome deve ser atualizado");
    assertEquals(originalEmail, originalEntity.getEmail(), "O email deve permanecer o mesmo");
    assertEquals(originalPhone, originalEntity.getPhone(), "O telefone deve permanecer o mesmo");
    assertEquals(originalPhotoId, originalEntity.getPhotoId(), "A foto deve permanecer a mesma");

    verify(gridFsTemplate, never()).delete(any(Query.class));
    verify(gridFsTemplate, never()).store(any(InputStream.class), any(String.class),
        any(String.class));
  }

  @Test
  @DisplayName("Deve deletar motorista com sucesso")
  void delete_ShouldReturnTrue_WhenUserExists() {
    driverDocument.setId("123");
    when(driverRepository.findByEmail(email)).thenReturn(Optional.of(driverDocument));

    Boolean deleted = driverService.deleteDriver(email);

    assertTrue(deleted, "A resposta deve ser True");
    verify(driverRepository, times(1)).deleteById("123");
  }

  @Test
  @DisplayName("Deve retornar exception ao tentar deletar motorista inexistente")
  void delete_ShouldReturnException_WhenUserNotFound() {
    when(driverRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.deleteDriver("fake@email.com");
    }, "A chamada deve retornr uma excecção devido a não haver um usuário cadastrado com esse email");

    verify(driverRepository, never()).deleteById(anyString());
  }

  @Test
  @DisplayName("Deve retornar DriverResponseDto quando o email existe")
  void findByEmail_ShouldReturnDto_WhenFound() {
    when(driverRepository.findByEmail(email)).thenReturn(Optional.of(driverDocument));

    DriverResponseDto result = driverService.findByEmail(email);

    assertNotNull(result, "A resposta não deve ser nula");

    assertEquals(email, result.email(),
        "E email do motorista cadastrado deve ser o mesmo que foi enviado");
  }

  @Test
  @DisplayName("Deve retornar exception quando o email não for encontrado")
  void findByEmail_ShouldReturnException_WhenNotFound() {
    when(driverRepository.findByEmail(email)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.findByEmail(email);
    }, "A chamada deve retornar uma exceção devido a não haver um usuário cadastrado com esse email");

  }

  @Test
  @DisplayName("Deve retornar o recurso da foto quando tudo estiver correto")
  void getPhotoResource_ShouldReturnResource() {
    DriverDocument driver = testDriverDocuments.entityFour();
    ObjectId photoId = new ObjectId();
    driver.setPhotoId(photoId);

    GridFSFile mockFile = mock(GridFSFile.class);
    GridFsResource mockResource = mock(GridFsResource.class);

    when(driverRepository.findByEmail(driver.getEmail())).thenReturn(Optional.of(driver));
    when(gridFsTemplate.findOne(any(Query.class))).thenReturn(mockFile);
    when(gridFsTemplate.getResource(mockFile)).thenReturn(mockResource);

    GridFsResource result = driverService.getPhotoResourceByDriverEmail(driver.getEmail());

    assertNotNull(result, "A resposta não pode ser nula");
    assertEquals(mockResource, result, "O recurso retornado deve ser igual ao recurso mockado");
    verify(gridFsTemplate).findOne(any(Query.class));
  }

  @Test
  @DisplayName("Deve retornar exception quando o email não pertencer a nenhum motorista")
  void getPhotoResource_ShouldReturnExceptionlWhenDriverNotFound() {
    String emailInexistente = "naoexiste@gmail.com";
    when(driverRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.getPhotoResourceByDriverEmail(emailInexistente);
    }, "A chamada deve retornar uma exceção devido a não haver um usuário cadastrado com esse email");

    verifyNoInteractions(gridFsTemplate);
  }

  @Test
  @DisplayName("Deve retornar exception quando o motorista não possuir photoId")
  void getPhotoResource_ShouldReturnExceptionWhenNoPhotoId() {
    DriverDocument driver = testDriverDocuments.entityFive();
    driver.setPhotoId(null);

    when(driverRepository.findByEmail(driver.getEmail())).thenReturn(Optional.of(driver));

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.getPhotoResourceByDriverEmail(driver.getEmail());
    }, "A chamada deve retornar uma exceção devido ao usuário cadastrado não ter foto cadastrada");


    verifyNoInteractions(gridFsTemplate);
  }

  @Test
  @DisplayName("Deve retornar exception quando o ID da foto existe mas o arquivo não foi encontrado no GridFS")
  void getPhotoResource_ShouldReturnExceptionWhenFileNotFoundInGridFS() {
    DriverDocument driver = testDriverDocuments.entitySix();
    driver.setPhotoId(new ObjectId());

    when(driverRepository.findByEmail(driver.getEmail())).thenReturn(Optional.of(driver));
    when(gridFsTemplate.findOne(any(Query.class))).thenReturn(null);

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.getPhotoResourceByDriverEmail(driver.getEmail());
    }, "A chamada deve retornar uma exceção devido ao usuário ter foto cadastrada mas ela não ser encontrada");

    verify(gridFsTemplate).findOne(any(Query.class));
    verify(gridFsTemplate, never()).getResource(any(GridFSFile.class));
  }


  @Test
  @DisplayName("Deve aprovar e ativar o cadastro do motorista")
  void verifyDocuments_ShouldApproveAllAndActivate() {
    DriverDocument driver = testDriverDocuments.entityThree();


    driver.setIsActive(false);

    when(driverRepository.findByEmail(driver.getEmail())).thenReturn(Optional.of(driver));
    when(driverRepository.save(any(DriverDocument.class))).thenAnswer(inv -> inv.getArgument(0));

    DriverResponseDto result = driverService.verifyDocuments(driver.getEmail());

    assertNotNull(result, "A resposta não pode ser nula");
    assertTrue(driver.getIsActive(), "O status do cadastro deve estar ATIVO");

    verify(driverRepository).save(driver);
  }


  @Test
  @DisplayName("Deve lançar exceção ResourceNotFoundException se o motorista a ser aprovado não for encontrado")
  void verifyDocuments_ShouldThrowException() {
    DriverDocument driver = testDriverDocuments.entityThree();

    driver.setIsActive(false);

    when(driverRepository.findByEmail(driver.getEmail())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.verifyDocuments(driver.getEmail());
    }, "A chamada deve retornar uma exceção devido não haver um motorista cadastrado com esse email");

    verify(driverRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve atualizar a senha com sucesso e criptografá-la")
  void updatePassword_ShouldReturnTrueWhenSuccessful() {
    DriverDocument driver = testDriverDocuments.entitySeven();
    String rawPassword = "NovaSenhaForte123";
    String encodedPassword = "hash_criptografado_aqui";
    PasswordUpdateRequest request = new PasswordUpdateRequest(rawPassword);

    when(driverRepository.findByEmail(driver.getEmail())).thenReturn(Optional.of(driver));
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
    when(driverRepository.save(any(DriverDocument.class))).thenAnswer(inv -> inv.getArgument(0));

    Boolean result = driverService.updatePassword(driver.getEmail(), request);

    assertTrue(result, "A resposta deve ser True");
    assertEquals(encodedPassword, driver.getPassword(),
        "A senha salva no documento deve ser a criptografada");

    verify(passwordEncoder).encode(rawPassword);
    verify(driverRepository).save(driver);
  }

  @Test
  @DisplayName("Deve retornar exception ao tentar atualizar senha de email inexistente")
  void updatePassword_ShouldReturnExceptionWhenUserNotFound() {
    String emailInexistente = "nao_existe@sumus.com";
    PasswordUpdateRequest request = new PasswordUpdateRequest("senha123");

    when(driverRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      driverService.updatePassword(emailInexistente, request);
    }, "A chamada deve retornar uma exceção devido a não haver um usuário cadastrado com esse email");

    verifyNoInteractions(passwordEncoder);
    verify(driverRepository, never()).save(any());
  }


}
