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

    @Field("password")
    private String password;

    @Field("email")
    private String email;

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

}
