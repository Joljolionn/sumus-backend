package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.request.PassengerRegistrationRequest;


@Component
public class TestPassengerDtos {

    public PassengerRegistrationRequest dtoOne() {
        return new PassengerRegistrationRequest("luzinete", "luzinete@gmail.com", "Luzinete@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoTwo() {
        return new PassengerRegistrationRequest("livia", "livia@gmail.com", "Livia@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoThree() {
        return new PassengerRegistrationRequest("alessandra", "alessandra@gmail.com", "Alessandra@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoFour() {
        return new PassengerRegistrationRequest("katia", "katia@gmail.com", "Katia@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoFive() {
        return new PassengerRegistrationRequest("rosangela", "rosangela@gmail.com", "Rosangela@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoSix() {
        return new PassengerRegistrationRequest("silvia", "silvia@gmail.com", "Silvia@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoSeven() {
        return new PassengerRegistrationRequest("luzinete", "luzinete@gmail.com", "Luzinete@123", "11 123456789", false, null);
    }

    public PassengerRegistrationRequest dtoEight() {
        return new PassengerRegistrationRequest("marialucia", "marialucia@gmail.com", "Marialucia@123", "11 123456789", false, null);
    }
}
