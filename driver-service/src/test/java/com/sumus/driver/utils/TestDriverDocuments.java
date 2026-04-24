package com.sumus.driver.utils;

import org.springframework.stereotype.Component;
import com.sumus.driver.domain.entities.DriverDocument;


@Component
public class TestDriverDocuments {

    public DriverDocument entityOne() {
        return new DriverDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 940028922", "123456789");
    }

    public DriverDocument entityTwo() {
        return new DriverDocument("livia", "livia@gmail.com", "livia123", "11 940028922", "123456789");
    }

    public DriverDocument entityThree() {
        return new DriverDocument("alessandra", "alessandra@gmail.com", "alessandra123", "11 940028922", "123456789");
    }

    public DriverDocument entityFour() {
        return new DriverDocument("katia", "katia@gmail.com", "katia123", "11 940028922", "123456789");
    }

    public DriverDocument entityFive() {
        return new DriverDocument("rosangela", "rosangela@gmail.com", "rosangela123", "11 940028922", "123456789");
    }

    public DriverDocument entitySix() {
        return new DriverDocument("silvia", "silvia@gmail.com", "silvia123", "11 940028922", "123456789");
    }

    public DriverDocument entitySeven() {
        return new DriverDocument("luzinete", "luzinete@gmail.com", "luzinete123", "11 940028922", "123456789");
    }

    public DriverDocument entityEight() {
        return new DriverDocument("marialucia", "marialucia@gmail.com", "marialucia123", "11 940028922", "123456789");
    }
}
