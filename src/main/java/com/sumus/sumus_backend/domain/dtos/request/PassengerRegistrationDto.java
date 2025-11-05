package com.sumus.sumus_backend.domain.dtos.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Classe usada na camada de apresentação para mostrar e transferir dados para
// os usuários
public class PassengerRegistrationDto {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve estar em um formato válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha deve conter no mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial (@$!%*?&).")
    private String password;

    @NotBlank(message = "O telefone é obrigatório")
    private String phone;

    private MultipartFile photo;

    @NotNull(message = "O usuário deve informar se é ou não prioritário")
    private Boolean isPcd;

    private List<String> conditions;

    public PassengerRegistrationDto() {
    }

    public PassengerRegistrationDto(String name, String email, String password, String phone, MultipartFile photo, Boolean isPcd, List<String> conditions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.photo = photo;
        this.isPcd = isPcd;
        this.conditions = conditions;
    }

    public PassengerRegistrationDto(String name, String email, String password, String phone) {
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

	public Boolean getIsPcd() {
		return isPcd;
	}

	public void setIsPcd(Boolean isPcd) {
		this.isPcd = isPcd;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}
}
