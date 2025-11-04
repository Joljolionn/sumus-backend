package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistrationDto;


@Component
public class TestDtos {

    public PassengerRegistrationDto dtoOne() {
        return new PassengerRegistrationDto("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public PassengerRegistrationDto dtoTwo() {
        return new PassengerRegistrationDto("livia", "livia@gmail.com", "livia123", "11 123456789");
    }

    public PassengerRegistrationDto dtoThree() {
        return new PassengerRegistrationDto("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789");
    }

    public PassengerRegistrationDto dtoFour() {
        return new PassengerRegistrationDto("katia", "katia@gmail.com", "katia123", "11 123456789");
    }

    public PassengerRegistrationDto dtoFive() {
        return new PassengerRegistrationDto("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789");
    }

    public PassengerRegistrationDto dtoSix() {
        return new PassengerRegistrationDto("silvia", "silvia@gmail.com", "silvia123", "11 123456789");
    }

    public PassengerRegistrationDto dtoSeven() {
        return new PassengerRegistrationDto("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public PassengerRegistrationDto dtoEight() {
        return new PassengerRegistrationDto("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789");
    }
}
