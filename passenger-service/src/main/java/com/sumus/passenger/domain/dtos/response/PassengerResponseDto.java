package com.sumus.passenger.domain.dtos.response;

import org.bson.types.ObjectId;

import com.sumus.passenger.domain.entities.PassengerDocument;


public class PassengerResponseDto {

	private String name;

	private String email;

	private String phone;

	private ObjectId photoId;

    public PassengerResponseDto() {
    }

    public PassengerResponseDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;

    }
    public PassengerResponseDto(PassengerDocument passengerDocument){
        this.name = passengerDocument.getName();
        this.email = passengerDocument.getEmail();
        this.phone = passengerDocument.getPhone();
        this.photoId = passengerDocument.getPhotoId();
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
