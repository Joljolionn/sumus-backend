package com.sumus.driver.domain.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;

public record DriverUpdateRequest(

    String name,

    @Email(message = "O email deve estar em um formato válido")
    String email,

    String phone,

    String cnh,

    MultipartFile photo

) {
}
