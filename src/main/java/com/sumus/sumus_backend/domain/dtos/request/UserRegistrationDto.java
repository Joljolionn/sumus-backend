package com.sumus.sumus_backend.domain.dtos.request;

import org.springframework.web.multipart.MultipartFile;

// Classe usada na camada de apresentação para mostrar e transferir dados para
// os usuários
public class UserRegistrationDto {

	public UserRegistrationDto() {
	}

	public UserRegistrationDto(String name, String email, String password, String phone, MultipartFile photo,
			String contentType, byte[] photoBytes) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.photo = photo;
		this.contentType = contentType;
		this.photoBytes = photoBytes;
	}

	public UserRegistrationDto(String name, String email, String password, String phone) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}

	private String name;

	private String email;

	private String password;

	private String phone;

	private MultipartFile photo;

	private String contentType;

	private byte[] photoBytes;

	public byte[] getFotoBytes() {
		return photoBytes;
	}

	public void setFotoBytes(byte[] photoBytes) {
		this.photoBytes = photoBytes;
	}

	public MultipartFile getFoto() {
		return photo;
	}

	public void setFoto(MultipartFile photo) {
		this.photo = photo;
	}

	public String getUsername() {
		return name;
	}

	public void setUsername(String name) {
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

	public String getTelefone() {
		return phone;
	}

	public void setTelefone(String phone) {
		this.phone = phone;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
