package com.sumus.passenger.utils;

import org.springframework.stereotype.Component;

import com.sumus.passenger.domain.entities.PassengerDocument;

@Component
public class TestPassengerDocuments {

    public PassengerDocument entityOne() {
        return new PassengerDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 940028922");
    }

    public PassengerDocument entityTwo() {
        return new PassengerDocument("livia", "livia@gmail.com", "livia123", "11 940028922");
    }

    public PassengerDocument entityThree() {
        return new PassengerDocument("alessandra", "alessandra@gmail.com", "alessandra123", "11 940028922");
    }

    public PassengerDocument entityFour() {
        return new PassengerDocument("katia", "katia@gmail.com", "katia123", "11 940028922");
    }

    public PassengerDocument entityFive() {
        return new PassengerDocument("rosangela", "rosangela@gmail.com", "rosangela123", "11 940028922");
    }

    public PassengerDocument entitySix() {
        return new PassengerDocument("silvia", "silvia@gmail.com", "silvia123", "11 940028922");
    }

    public PassengerDocument entitySeven() {
        return new PassengerDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 940028922");
    }

    public PassengerDocument entityEight() {
        return new PassengerDocument("marialucia", "marialucia@gmail.com", "marialucia123", "11 940028922");
    }
}
