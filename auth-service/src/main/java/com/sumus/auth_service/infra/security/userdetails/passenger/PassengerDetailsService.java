package com.sumus.auth_service.infra.security.userdetails.passenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sumus.auth_service.repositories.PassengerRepository;



@Service
@Primary
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
