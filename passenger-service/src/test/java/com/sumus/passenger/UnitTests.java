package com.sumus.passenger;

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
import com.sumus.passenger.domain.dtos.request.PassengerRegistrationRequest;
import com.sumus.passenger.domain.dtos.request.PassengerUpdateRequest;
import com.sumus.passenger.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.passenger.domain.dtos.response.PassengerListResponseDto;
import com.sumus.passenger.domain.dtos.response.PassengerResponseDto;
import com.sumus.passenger.domain.entities.PassengerDocument;
import com.sumus.passenger.domain.entities.PcdCondition;
import com.sumus.passenger.repositories.PassengerRepository;
import com.sumus.passenger.services.impl.PassengerServiceImpl;
import com.sumus.passenger.utils.TestPassengerDocuments;

@ExtendWith(MockitoExtension.class)
class UnitTests {

  @Mock
  private PassengerRepository passengerRepository;

  @Mock
  private GridFsTemplate gridFsTemplate;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private PassengerServiceImpl passengerService;


  private TestPassengerDocuments testPassengerDocuments;
  private PassengerDocument passengerDocument;
  private String email;

  @BeforeEach
  void setUp() {
    testPassengerDocuments = new TestPassengerDocuments();
    passengerDocument = testPassengerDocuments.entityOne();
    email = passengerDocument.getEmail();
  }

  @Test
  @DisplayName("Deve criar um passageiro com sucesso")
  void create_ShouldReturnResponseDto_WhenSuccess() throws IOException {
    PassengerRegistrationRequest request = new PassengerRegistrationRequest(email, "password123");

    when(passengerRepository.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(passengerRepository.save(any(PassengerDocument.class))).thenReturn(passengerDocument);

    PassengerResponseDto result = passengerService.create(request);

    assertNotNull(result, "A resposta não deve ser nula");
    assertEquals(email, result.email(),
        "O email da resposta retornada deve ser igual ao email enviado");
    verify(passengerRepository, times(1)).save(any(PassengerDocument.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao criar passageiro com e-mail já existente")
  void create_ShouldThrowException_WhenEmailExists() {
    PassengerRegistrationRequest request = new PassengerRegistrationRequest(email, "password123");
    when(passengerRepository.existsByEmail(email)).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> {
      passengerService.create(request);
    }, "A chamada deve retornar uma exceção devido a já haver um usuário cadastrado com esse email");

    verify(passengerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve retornar corretamente uma lista com todos os usuários cadastrados")
  void listAll_ShoudReturnPassengerList() {
    List<PassengerDocument> mockList = new ArrayList<>();
    mockList.addLast(testPassengerDocuments.entityOne());
    mockList.addLast(testPassengerDocuments.entityTwo());
    mockList.addLast(testPassengerDocuments.entityThree());
    when(passengerRepository.findAll()).thenReturn(mockList);

    PassengerListResponseDto result = passengerService.listAll();

    assertNotNull(result, "A resposta não pode ser nula");
    assertEquals(3, result.passengers().size(), "A quantidade de passageiros deve ser 3");

    assertEquals(mockList.get(0).getEmail(), result.passengers().get(0).email());
    assertEquals(mockList.get(0).getName(), result.passengers().get(0).name());

    List<String> emailsEsperados = mockList.stream().map(PassengerDocument::getEmail).toList();
    List<String> emailsRetornados =
        result.passengers().stream().map(PassengerResponseDto::email).toList();

    assertEquals(emailsEsperados, emailsRetornados,
        "Todos os emails devem coincidir na ordem correta");
  }

  @Test
  @DisplayName("Deve ser capaz de atualizar por completo o usuário cadastrado")
  void update_ShouldUpdateAllFields() throws IOException {
    PassengerDocument originalEntity = testPassengerDocuments.entityOne();
    originalEntity.setPhotoId(new ObjectId());

    PassengerDocument updatedData = testPassengerDocuments.entityTwo();
    MockMultipartFile newPhoto =
        new MockMultipartFile("photo", "new.jpg", "image/jpeg", "content".getBytes());

    PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(updatedData.getName(),
        updatedData.getEmail(), updatedData.getPhone(), newPhoto);

    ObjectId newPhotoId = new ObjectId();

    when(passengerRepository.findByEmail(email)).thenReturn(Optional.of(originalEntity));
    when(gridFsTemplate.store(any(InputStream.class), any(String.class), any(String.class)))
        .thenReturn(newPhotoId);
    when(passengerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    PassengerResponseDto result = passengerService.update(email, updateRequest);

    assertNotNull(result, "A resposta retornado não deve ser nula");

    assertEquals(updatedData.getName(), result.name(), "O nome deve ser atualizado");

    assertEquals(updatedData.getEmail(), result.email(), "O email deve ser atualizado");

    assertEquals(newPhotoId, originalEntity.getPhotoId(), "O ID da foto deve ser atualizado");

    verify(gridFsTemplate, times(1)).delete(any(Query.class));
    verify(gridFsTemplate, times(1)).store(any(InputStream.class), any(String.class),
        any(String.class));
    verify(passengerRepository).save(originalEntity);
  }

  @Test
  @DisplayName("Deve ser capaz de atualizar somente um atributo do usuário")
  void update_ShouldUpdateOnlyName() throws IOException {
    PassengerDocument originalEntity = testPassengerDocuments.entityThree();
    String originalEmail = originalEntity.getEmail();
    String originalPhone = originalEntity.getPhone();
    ObjectId originalPhotoId = new ObjectId();
    originalEntity.setPhotoId(originalPhotoId);

    String newName = testPassengerDocuments.entitySix().getName();
    PassengerUpdateRequest updateRequest = new PassengerUpdateRequest(newName, null, null, null);

    when(passengerRepository.findByEmail(originalEmail)).thenReturn(Optional.of(originalEntity));
    when(passengerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    PassengerResponseDto result = passengerService.update(originalEmail, updateRequest);

    assertEquals(newName, result.name(), "O nome deve ser atualizado");
    assertEquals(originalEmail, originalEntity.getEmail(), "O email deve permanecer o mesmo");
    assertEquals(originalPhone, originalEntity.getPhone(), "O telefone deve permanecer o mesmo");
    assertEquals(originalPhotoId, originalEntity.getPhotoId(), "A foto deve permanecer a mesma");

    verify(gridFsTemplate, never()).delete(any(Query.class));
    verify(gridFsTemplate, never()).store(any(InputStream.class), any(String.class),
        any(String.class));
  }

  @Test
  @DisplayName("Deve deletar passageiro com sucesso")
  void delete_ShouldReturnTrue_WhenUserExists() {
    passengerDocument.setId("123");
    when(passengerRepository.findByEmail(email)).thenReturn(Optional.of(passengerDocument));

    Boolean deleted = passengerService.delete(email);

    assertTrue(deleted, "A resposta deve ser True");
    verify(passengerRepository, times(1)).deleteById("123");
  }

  @Test
  @DisplayName("Deve retornar false ao tentar deletar usuário inexistente")
  void delete_ShouldReturnFalse_WhenUserNotFound() {
    when(passengerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    Boolean deleted = passengerService.delete("fake@email.com");

    assertFalse(deleted, "A resposta deve ser False");
    verify(passengerRepository, never()).deleteById(anyString());
  }

  @Test
  @DisplayName("Deve retornar PassengerResponseDto quando o email existe")
  void findByEmail_ShouldReturnDto_WhenFound() {
    when(passengerRepository.findByEmail(email)).thenReturn(Optional.of(passengerDocument));

    PassengerResponseDto result = passengerService.findByEmail(email);

    assertNotNull(result, "A resposta não deve ser nula");

    assertEquals(email, result.email(),
        "E email do passageiro cadastrado deve ser o mesmo que foi enviado");
  }

  @Test
  @DisplayName("Deve retornar Null quando o email não for encontrado")
  void findByEmail_ShouldReturnNull_WhenNotFound() {
    when(passengerRepository.findByEmail(email)).thenReturn(Optional.empty());

    PassengerResponseDto result = passengerService.findByEmail(email);

    assertNull(result, "A resposta deve ser nula");
  }

  @Test
  @DisplayName("Deve retornar o recurso da foto quando tudo estiver correto")
  void getPhotoResource_ShouldReturnResource() {
    PassengerDocument passenger = testPassengerDocuments.entityFour();
    ObjectId photoId = new ObjectId();
    passenger.setPhotoId(photoId);

    GridFSFile mockFile = mock(GridFSFile.class);
    GridFsResource mockResource = mock(GridFsResource.class);

    when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));
    when(gridFsTemplate.findOne(any(Query.class))).thenReturn(mockFile);
    when(gridFsTemplate.getResource(mockFile)).thenReturn(mockResource);

    GridFsResource result = passengerService.getPhotoResourceByPassengerEmail(passenger.getEmail());

    assertNotNull(result, "A resposta não pode ser nula");
    assertEquals(mockResource, result, "O recurso retornado deve ser igual ao recurso mockado");
    verify(gridFsTemplate).findOne(any(Query.class));
  }

  @Test
  @DisplayName("Deve retornar null quando o email não pertencer a nenhum passageiro")
  void getPhotoResource_ShouldReturnNullWhenPassengerNotFound() {
    String emailInexistente = "naoexiste@gmail.com";
    when(passengerRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

    GridFsResource result = passengerService.getPhotoResourceByPassengerEmail(emailInexistente);

    assertNull(result, "A resposta deve ser nula");
    verifyNoInteractions(gridFsTemplate);
  }

  @Test
  @DisplayName("Deve retornar null quando o passageiro não possuir photoId")
  void getPhotoResource_ShouldReturnNullWhenNoPhotoId() {
    PassengerDocument passenger = testPassengerDocuments.entityFive();
    passenger.setPhotoId(null);

    when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));

    GridFsResource result = passengerService.getPhotoResourceByPassengerEmail(passenger.getEmail());

    assertNull(result, "A resposta deve ser nula");
    verifyNoInteractions(gridFsTemplate);
  }

  @Test
  @DisplayName("Deve retornar null quando o ID da foto existe mas o arquivo não foi encontrado no GridFS")
  void getPhotoResource_ShouldReturnNullWhenFileNotFoundInGridFS() {
    PassengerDocument passenger = testPassengerDocuments.entitySix();
    passenger.setPhotoId(new ObjectId());

    when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));
    when(gridFsTemplate.findOne(any(Query.class))).thenReturn(null);

    GridFsResource result = passengerService.getPhotoResourceByPassengerEmail(passenger.getEmail());

    assertNull(result, "A resposta deve ser nula");
    verify(gridFsTemplate).findOne(any(Query.class));
    verify(gridFsTemplate, never()).getResource(any(GridFSFile.class));
  }

  @Test
  @DisplayName("Deve retornar true quando o passageiro estiver com status ATIVO")
  void getActiveStatus_ShouldReturnTrue() {
    PassengerDocument passenger = testPassengerDocuments.entityOne();
    passenger.setStatusCadastro(PassengerDocument.StatusCadastro.ATIVO);

    when(passengerRepository.findByEmail(email)).thenReturn(Optional.of(passenger));

    Boolean isActive = passengerService.getActiveStatus(email);

    assertTrue(isActive, "A resposta deve ser True");
  }

  @Test
  @DisplayName("Deve retornar false quando o passageiro não estiver ATIVO")
  void getActiveStatus_ShouldReturnFalse() {
    PassengerDocument passenger = testPassengerDocuments.entityTwo();
    passenger.setStatusCadastro(PassengerDocument.StatusCadastro.PENDENTE_PCD);

    when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));

    Boolean isActive = passengerService.getActiveStatus(passenger.getEmail());

    assertFalse(isActive, "A resposta deve ser False");
  }

  @Test
  @DisplayName("Deve retornar null quando o passageiro não for encontrado")
  void getActiveStatus_ShouldReturnNull() {
    when(passengerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    Boolean isActive = passengerService.getActiveStatus("ghost@email.com");

    assertNull(isActive, "A resposta deve ser nula");
  }

  @Test
  @DisplayName("Deve aprovar todas as condições PCD e ativar o cadastro do passageiro")
  void verifyPcdConditions_ShouldApproveAllAndActivate() {
    PassengerDocument passenger = testPassengerDocuments.entityThree();

    PcdCondition cond1 = new PcdCondition();
    cond1.setValidationStatus(PcdCondition.ValidationStatus.PENDENTE);
    PcdCondition cond2 = new PcdCondition();
    cond2.setValidationStatus(PcdCondition.ValidationStatus.PENDENTE);

    passenger.setPcdConditions(List.of(cond1, cond2));
    passenger.setStatusCadastro(PassengerDocument.StatusCadastro.PENDENTE_PCD);

    when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));
    when(passengerRepository.save(any(PassengerDocument.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    PassengerResponseDto result = passengerService.verifyPcdConditions(passenger.getEmail());

    assertNotNull(result, "A resposta não pode ser nula");
    assertEquals(PassengerDocument.StatusCadastro.ATIVO, passenger.getStatusCadastro(), "O status do cadastro deve estar ATIVO");

    assertTrue(
        passenger.getPcdConditions().stream()
            .allMatch(c -> c.getValidationStatus() == PcdCondition.ValidationStatus.APROVADO),
        "Todas as condições devem estar aprovadas");

    verify(passengerRepository).save(passenger);
  }

  @Test
  @DisplayName("Deve atualizar a senha com sucesso e criptografá-la")
  void updatePassword_ShouldReturnTrueWhenSuccessful() {
    PassengerDocument passenger = testPassengerDocuments.entitySeven();
    String rawPassword = "NovaSenhaForte123";
    String encodedPassword = "hash_criptografado_aqui";
    PasswordUpdateRequest request = new PasswordUpdateRequest(rawPassword);

    when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(Optional.of(passenger));
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
    when(passengerRepository.save(any(PassengerDocument.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    Boolean result = passengerService.updatePassword(passenger.getEmail(), request);

    assertTrue(result, "A resposta deve ser True");
    assertEquals(encodedPassword, passenger.getPassword(),
        "A senha salva no documento deve ser a criptografada");

    verify(passwordEncoder).encode(rawPassword);
    verify(passengerRepository).save(passenger);
  }

  @Test
  @DisplayName("Deve retornar false ao tentar atualizar senha de email inexistente")
  void updatePassword_ShouldReturnFalseWhenUserNotFound() {
    String emailInexistente = "nao_existe@sumus.com";
    PasswordUpdateRequest request = new PasswordUpdateRequest("senha123");

    when(passengerRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

    Boolean result = passengerService.updatePassword(emailInexistente, request);

    assertFalse(result, "A resposta deve ser False");
    verifyNoInteractions(passwordEncoder);
    verify(passengerRepository, never()).save(any());
  }


}
