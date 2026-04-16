package com.sumus.sumus_backend.domain.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Classe para representar como o request de login será recebido
public class LoginRequest {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve estar em um formato válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    public LoginRequest(){}

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
