package com.sumus.admin_service.domain.models;

import java.time.LocalDate;
import org.bson.types.ObjectId;
import com.sumus.admin_service.infra.persistence.mongodb.documents.AdminDocument.AdminLevel;

public class Admin {

  private String id;

  private String name;

  private String email;

  private String password;

  private String phone;

  private ObjectId photoId;

  private String cpf;

  private LocalDate dataNasc;

  private AdminLevel adminLevel;

  public Admin(String id, String name, String email, String phone, ObjectId photoId, String cpf,
      LocalDate dataNasc, AdminLevel adminLevel) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.photoId = photoId;
    this.cpf = cpf;
    this.dataNasc = dataNasc;
    this.adminLevel = adminLevel;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getPhone() {
    return phone;
  }

  public ObjectId getPhotoId() {
    return photoId;
  }

  public String getCpf() {
    return cpf;
  }

  public LocalDate getDataNasc() {
    return dataNasc;
  }

  public AdminLevel getAdminLevel() {
    return adminLevel;
  }

}
