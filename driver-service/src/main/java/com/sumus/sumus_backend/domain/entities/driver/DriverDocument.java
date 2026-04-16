package com.sumus.sumus_backend.domain.entities.driver;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "drivers")
public class DriverDocument {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("phone")
    private String phone;

    @Field("cnh")
    private String cnh;

    @Field("photoId")
    private ObjectId photoId;

    @Field("crlv")
    private ObjectId crlv;

    @Field("statusCadastro")
    private Boolean isActive;

    public DriverDocument(String name, String email, String password, String phone, String cnh){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.cnh = cnh;
        this.isActive = false;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCnh() {
		return cnh;
	}

	public void setCnh(String cnh) {
		this.cnh = cnh;
	}

	public ObjectId getPhotoId() {
		return photoId;
	}

	public void setPhotoId(ObjectId photoId) {
		this.photoId = photoId;
	}

	public ObjectId getCrlv() {
		return crlv;
	}

	public void setCrlv(ObjectId crlv) {
		this.crlv = crlv;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
