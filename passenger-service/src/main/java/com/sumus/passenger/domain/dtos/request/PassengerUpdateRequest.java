package com.sumus.passenger.domain.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;

public record PassengerUpdateRequest(

    String name,

    @Email(message = "O email deve estar em um formato válido")
    String email,

    String phone,

    MultipartFile photo

) {
}
