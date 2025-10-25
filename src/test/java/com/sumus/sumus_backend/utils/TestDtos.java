package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.request.UserRegistrationDto;


@Component
public class TestDtos {

    public UserRegistrationDto dtoOne() {
        return new UserRegistrationDto("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public UserRegistrationDto dtoTwo() {
        return new UserRegistrationDto("livia", "livia@gmail.com", "livia123", "11 123456789");
    }

    public UserRegistrationDto dtoThree() {
        return new UserRegistrationDto("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789");
    }

    public UserRegistrationDto dtoFour() {
        return new UserRegistrationDto("katia", "katia@gmail.com", "katia123", "11 123456789");
    }

    public UserRegistrationDto dtoFive() {
        return new UserRegistrationDto("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789");
    }

    public UserRegistrationDto dtoSix() {
        return new UserRegistrationDto("silvia", "silvia@gmail.com", "silvia123", "11 123456789");
    }

    public UserRegistrationDto dtoSeven() {
        return new UserRegistrationDto("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public UserRegistrationDto dtoEight() {
        return new UserRegistrationDto("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789");
    }
}
