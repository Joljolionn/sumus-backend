package com.sumus.driver.infra.security.util;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

  DRIVER("ROLE_DRIVER");

  private String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

}
