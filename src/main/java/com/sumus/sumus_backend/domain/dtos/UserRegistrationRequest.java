package com.sumus.sumus_backend.domain.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

public class UserRegistrationRequest {
    @Valid
    private UserDto userDto;
    private MultipartFile photo;

	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
	public MultipartFile getPhoto() {
		return photo;
	}
	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}
}
