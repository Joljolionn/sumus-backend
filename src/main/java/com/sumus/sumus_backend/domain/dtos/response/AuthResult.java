package com.sumus.sumus_backend.domain.dtos.response;

// Classe para representar a resposta para pedido de auth de usuário
public class AuthResult {

    public enum Status {
        USER_NOT_FOUND, INVALID_PASSWORD, SUCCESS
    };

    public AuthResult(Status status, String token){
        this.status = status;
        this.token = token;
    }

    private Status status;
    private String token;

	public Status getStatus() {
		return status;
	}
	public String getToken() {
		return token;
	}

}
