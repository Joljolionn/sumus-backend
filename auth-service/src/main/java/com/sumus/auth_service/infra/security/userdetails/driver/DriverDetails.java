package com.sumus.auth_service.infra.security.userdetails.driver;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.sumus.auth_service.domain.documents.DriverAuthDocument;
import com.sumus.auth_service.infra.security.util.UserRole;


public class DriverDetails implements UserDetails {

    private DriverAuthDocument driverDocument;

    public DriverDetails (DriverAuthDocument driverDocument) {
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
