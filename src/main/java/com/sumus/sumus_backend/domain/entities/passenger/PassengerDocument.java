package com.sumus.sumus_backend.domain.entities.passenger;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "passengers")
public class PassengerDocument {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Indexed(unique = true)
    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("phone")
    private String phone;

    @Field("photoId")
    private ObjectId photoId;

    @Field("isPcd")
    private Boolean isPcd = false;

    @Field("statusCadastro")
    private StatusCadastro statusCadastro;

    @Field("pcdConditions")
    private List<PcdCondition> pcdConditions;

	public enum StatusCadastro {
        ATIVO, PENDENTE_PCD, REPROVADO_PCD
    }

    public PassengerDocument() {
    }

    public PassengerDocument(String name, String email, String password, String phone, Boolean isPcd, List<PcdCondition> pcdConditions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isPcd = isPcd;
        this.statusCadastro = isPcd ? StatusCadastro.PENDENTE_PCD : StatusCadastro.ATIVO;
        this.pcdConditions = pcdConditions;
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

    public Boolean getIsPcd() {
        return isPcd;
    }

    public void setIsPcd(Boolean isPcd) {
        this.isPcd = isPcd;
    }

    public ObjectId getPhotoId() {
        return photoId;
    }

    public void setPhotoId(ObjectId photoId) {
        this.photoId = photoId;
    }

    public List<PcdCondition> getPcdConditions() {
        return pcdConditions;
    }

    public void setPcdConditions(List<PcdCondition> pcdConditions) {
        this.pcdConditions = pcdConditions;
    }

    public StatusCadastro getStatusCadastro() {
        return statusCadastro;
    }

    public void setStatusCadastro(StatusCadastro statusCadastro) {
        this.statusCadastro = statusCadastro;
    }
}
