package com.sumus.sumus_backend.infra.security.userdetails.passenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sumus.sumus_backend.repositories.passenger.PassengerRepository;

@Service
public class PassengerDetailsService implements UserDetailsService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PassengerDetails(passengerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger " + username + " not found!")));
        // TODO: Adicionar em todos os Optionals essa exceção acima ^
    }

}
