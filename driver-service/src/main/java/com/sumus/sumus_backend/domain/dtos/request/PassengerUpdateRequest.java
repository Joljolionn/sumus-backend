package com.sumus.sumus_backend.domain.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;

public class PassengerUpdateRequest {

    private String name;

    @Email(message = "O email deve estar em um formato válido")
    private String email;

    private String phone;

    private MultipartFile photo;

    public PassengerUpdateRequest() {
    }

    public PassengerUpdateRequest(String name, String email, String phone, MultipartFile photo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
