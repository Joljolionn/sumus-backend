package com.sumus.sumus_backend.domain.dtos.response;

import org.bson.types.ObjectId;

import com.sumus.sumus_backend.domain.entities.driver.DriverDocument;


public class DriverResponseDto {

	private String name;

	private String email;

	private String phone;

	private ObjectId photoId;

    public DriverResponseDto() {
    }

    public DriverResponseDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public DriverResponseDto(DriverDocument driverDocument){
        this.name = driverDocument.getName();
        this.email = driverDocument.getEmail();
        this.phone = driverDocument.getPhone();
        this.photoId = driverDocument.getPhotoId();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return phone;
	}

	public void setTelefone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ObjectId getPhotoId() {
		return photoId;
	}

	public void setPhotoId(ObjectId photoId) {
		this.photoId = photoId;
	}
}
