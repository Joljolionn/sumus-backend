package com.sumus.passenger.infra.security.userdetails;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sumus.passenger.domain.entities.PassengerDocument;
import com.sumus.passenger.infra.security.util.UserRole;

public class PassengerDetails implements UserDetails {
    
    private PassengerDocument passengerDocument;

    public PassengerDetails(PassengerDocument passengerDocument){
        this.passengerDocument = passengerDocument;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(UserRole.PASSENGER);
	}

	@Override
	public String getPassword() {
        return this.passengerDocument.getPassword();
	}

	@Override
	public String getUsername() {
        return this.passengerDocument.getEmail();
	}

    
}
