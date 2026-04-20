package com.sumus.driver.domain.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve estar em um formato válido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    String password

) {
}
