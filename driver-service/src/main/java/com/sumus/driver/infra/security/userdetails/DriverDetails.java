package com.sumus.driver.infra.security.userdetails;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sumus.driver.domain.entities.DriverDocument;
import com.sumus.driver.infra.security.util.UserRole;


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
