package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.entities.UserDocument;

@Component
public class TestEntities {

    public UserDocument entityOne() {
        return new UserDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789", "passageiro");
    }

    public UserDocument entityTwo() {
        return new UserDocument("livia", "livia@gmail.com", "livia123", "11 123456789", "passageiro");
    }

    public UserDocument entityThree() {
        return new UserDocument("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789", "passageiro");
    }

    public UserDocument entityFour() {
        return new UserDocument("katia", "katia@gmail.com", "katia123", "11 123456789", "passageiro");
    }

    public UserDocument entityFive() {
        return new UserDocument("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789", "passageiro");
    }

    public UserDocument entitySix() {
        return new UserDocument("silvia", "silvia@gmail.com", "silvia123", "11 123456789", "passageiro");
    }

    public UserDocument entitySeven() {
        return new UserDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789", "passageiro");
    }

    public UserDocument entityEight() {
        return new UserDocument("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789", "passageiro");
    }
}
