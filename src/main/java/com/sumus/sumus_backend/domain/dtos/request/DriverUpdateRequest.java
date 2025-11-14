package com.sumus.sumus_backend.domain.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;

public class DriverUpdateRequest {

    private String name;

    @Email(message = "O email deve estar em um formato válido")
    private String email;

    private String phone;

    private String cnh;

    private MultipartFile photo;

    public DriverUpdateRequest() {
    }

    public DriverUpdateRequest(String name, String email, String phone, String cnh,
            MultipartFile photo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cnh = cnh;
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

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

}
