package com.sumus.sumus_backend.domain.dtos.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Classe usada na camada de apresentação para mostrar e transferir dados para
// os usuários
public class PassengerRegistrationRequest {

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "O email deve estar em um formato válido")
  private String email;

  @NotBlank(message = "A senha é obrigatória.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha deve conter no mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial (@$!%*?&).")
  private String password;

  public PassengerRegistrationRequest() {
  }

  public PassengerRegistrationRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
