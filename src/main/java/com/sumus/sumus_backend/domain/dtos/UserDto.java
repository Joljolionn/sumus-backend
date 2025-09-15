package com.sumus.sumus_backend.domain.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;

public class UserDto {

	public UserDto() {
	}

	private String username;

	private String email;

	private String password;

	private String telefone;

	private MultipartFile foto;

	private String contentType;

	private byte[] fotoBytes;

	public byte[] getFotoBytes() {
		return fotoBytes;
	}

	public void setFotoBytes(byte[] fotoBytes) {
		this.fotoBytes = fotoBytes;
	}

	public MultipartFile getFoto() {
		return foto;
	}

	public void setFoto(MultipartFile foto) {
		this.foto = foto;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
