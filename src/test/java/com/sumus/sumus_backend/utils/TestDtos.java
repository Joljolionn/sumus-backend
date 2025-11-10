package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistration;


@Component
public class TestDtos {

    public PassengerRegistration dtoOne() {
        return new PassengerRegistration("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public PassengerRegistration dtoTwo() {
        return new PassengerRegistration("livia", "livia@gmail.com", "livia123", "11 123456789");
    }

    public PassengerRegistration dtoThree() {
        return new PassengerRegistration("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789");
    }

    public PassengerRegistration dtoFour() {
        return new PassengerRegistration("katia", "katia@gmail.com", "katia123", "11 123456789");
    }

    public PassengerRegistration dtoFive() {
        return new PassengerRegistration("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789");
    }

    public PassengerRegistration dtoSix() {
        return new PassengerRegistration("silvia", "silvia@gmail.com", "silvia123", "11 123456789");
    }

    public PassengerRegistration dtoSeven() {
        return new PassengerRegistration("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public PassengerRegistration dtoEight() {
        return new PassengerRegistration("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789");
    }
}
