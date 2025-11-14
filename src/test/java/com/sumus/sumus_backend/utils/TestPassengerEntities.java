package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;


@Component
public class TestPassengerEntities {

    public PassengerDocument entityOne() {
        return new PassengerDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789", false, null);
    }

    public PassengerDocument entityTwo() {
        return new PassengerDocument("livia", "livia@gmail.com", "livia123", "11 123456789", false, null);
    }

    public PassengerDocument entityThree() {
        return new PassengerDocument("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789", false, null);
    }

    public PassengerDocument entityFour() {
        return new PassengerDocument("katia", "katia@gmail.com", "katia123", "11 123456789", false, null);
    }

    public PassengerDocument entityFive() {
        return new PassengerDocument("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789", false, null);
    }

    public PassengerDocument entitySix() {
        return new PassengerDocument("silvia", "silvia@gmail.com", "silvia123", "11 123456789", false, null);
    }

    public PassengerDocument entitySeven() {
        return new PassengerDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789", false, null);
    }

    public PassengerDocument entityEight() {
        return new PassengerDocument("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789", false, null);
    }
}
