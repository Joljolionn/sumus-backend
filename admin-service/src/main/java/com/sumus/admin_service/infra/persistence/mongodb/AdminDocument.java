package com.sumus.admin_service.infra.persistence.mongodb;

import java.time.LocalDate;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
public class AdminDocument {

  @Id
  private String id;

  private String name;

  @Indexed(unique = true)
  private String email;

  private String password;

  private String phone;

  private ObjectId photoId;

  @Indexed(unique = true)
  private String cpf;

  private LocalDate dataNasc;

  private AdminLevel adminLevel;


  public enum AdminLevel {
    PRIMARY, SECONDARY
  }

  public AdminDocument(String name, String email, String password, String phone,
      String cpf, LocalDate dataNasc) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.cpf = cpf;
    this.dataNasc = dataNasc;
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


  public ObjectId getPhotoId() {
    return photoId;
  }


  public void setPhotoId(ObjectId photoId) {
    this.photoId = photoId;
  }


  public String getCpf() {
    return cpf;
  }


  public void setCpf(String cpf) {
    this.cpf = cpf;
  }


  public LocalDate getDataNasc() {
    return dataNasc;
  }


  public void setDataNasc(LocalDate dataNasc) {
    this.dataNasc = dataNasc;
  }

  public AdminLevel getAdminLevel() {
    return adminLevel;
  }

  public void setAdminLevel(AdminLevel adminLevel) {
    this.adminLevel = adminLevel;
  }

}
