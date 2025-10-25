package com.sumus.sumus_backend.domain.dtos.request;

import org.springframework.web.multipart.MultipartFile;

// Classe usada na camada de apresentação para mostrar e transferir dados para
// os usuários
public class UserRegistrationDto {

    private String name;

    private String email;

    private String password;

    private String phone;

    private MultipartFile photo;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String name, String email, String password, String phone, MultipartFile photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.photo = photo;
    }

    public UserRegistrationDto(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
