package com.sumus.sumus_backend.domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed; // NOVA IMPORTAÇÃO AQUI!

@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String telefone;

    private byte[] foto;

    @Field("content_type")
    private String contentType;

    private String tipo;

    public UserDocument() {
    }

    public UserDocument(String id, String tipo, String username, String email, String password, String telefone,
            byte[] foto, String contentType) {
        this.id = id;
        this.tipo = tipo;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telefone = telefone;
        this.foto = foto;
        this.contentType = contentType;
    }

    public UserDocument(String username, String email, String password, String telefone, String tipo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telefone = telefone;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public void setFotoPath(byte[] foto) {
        this.foto = foto;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
