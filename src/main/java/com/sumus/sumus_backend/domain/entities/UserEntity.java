package com.sumus.sumus_backend.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
        private Long id;

        @Column(nullable = false)
        private String username;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String telefone;

        @Column(nullable = true)
        private byte[] foto;

        @Column(nullable = true)
        private String contentType;

        public void setFoto(byte[] foto) {
                this.foto = foto;
        }

        public String getContentType() {
                return contentType;
        }

        public void setContentType(String contentType) {
                this.contentType = contentType;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
       }

        public void setUsername(String nome) {
                this.username = nome;
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

        public void setPassword(String senha) {
                this.password = senha;
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

        public void setFotoPath(byte[] foto) {
                this.foto = foto;
        }

        private String tipo;

}
