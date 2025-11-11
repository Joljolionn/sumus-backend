package com.sumus.sumus_backend.infra.security.util;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    DRIVER("driver"),
    PASSENGER("passenger");


	private String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

	@Override
	public String getAuthority() {
        return authority;
	}

}
