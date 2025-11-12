package com.sumus.sumus_backend.infra.security.userdetails.driver;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sumus.sumus_backend.domain.entities.driver.DriverDocument;
import com.sumus.sumus_backend.infra.security.util.UserRole;

public class DriverDetails implements UserDetails {

    private DriverDocument driverDocument;

    public DriverDetails (DriverDocument driverDocument) {
        this.driverDocument = driverDocument;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(UserRole.DRIVER);
	}

	@Override
	public String getPassword() {
        return this.driverDocument.getPassword();
	}

	@Override
	public String getUsername() {
	    return this.driverDocument.getEmail();
	}

    
}
