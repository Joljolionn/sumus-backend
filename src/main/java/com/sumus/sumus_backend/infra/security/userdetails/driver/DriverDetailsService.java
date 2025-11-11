package com.sumus.sumus_backend.infra.security.userdetails.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sumus.sumus_backend.repositories.driver.DriverRepository;

public class DriverDetailsService implements UserDetailsService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new DriverDetails(driverRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Driver " + username + "not found!")));
    }

}
