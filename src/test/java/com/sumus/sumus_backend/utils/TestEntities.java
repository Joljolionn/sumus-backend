package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.entities.UserDocuments;

@Component
public class TestEntities {

    public UserDocuments entityOne() {
        return new UserDocuments("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789", "passageiro");
    }

    public UserDocuments entityTwo() {
        return new UserDocuments("livia", "livia@gmail.com", "livia123", "11 123456789", "passageiro");
    }

    public UserDocuments entityThree() {
        return new UserDocuments("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789", "passageiro");
    }

    public UserDocuments entityFour() {
        return new UserDocuments("katia", "katia@gmail.com", "katia123", "11 123456789", "passageiro");
    }

    public UserDocuments entityFive() {
        return new UserDocuments("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789", "passageiro");
    }

    public UserDocuments entitySix() {
        return new UserDocuments("silvia", "silvia@gmail.com", "silvia123", "11 123456789", "passageiro");
    }

    public UserDocuments entitySeven() {
        return new UserDocuments("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789", "passageiro");
    }

    public UserDocuments entityEight() {
        return new UserDocuments("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789", "passageiro");
    }
}
