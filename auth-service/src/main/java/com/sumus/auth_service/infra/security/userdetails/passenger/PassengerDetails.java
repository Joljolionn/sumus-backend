package com.sumus.auth_service.infra.security.userdetails.passenger;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.sumus.auth_service.domain.documents.PassengerAuthDocument;
import com.sumus.auth_service.infra.security.util.UserRole;


public class PassengerDetails implements UserDetails {

  private PassengerAuthDocument passenger;

  public PassengerDetails(PassengerAuthDocument passenger) {
    this.passenger = passenger;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(UserRole.PASSENGER);
  }

  @Override
  public String getPassword() {
    return this.passenger.getPassword();
  }

  @Override
  public String getUsername() {
    return this.passenger.getEmail();
  }


}
