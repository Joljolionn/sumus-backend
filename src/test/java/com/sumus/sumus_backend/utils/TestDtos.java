package com.sumus.sumus_backend.utils;

import org.springframework.stereotype.Component;

import com.sumus.sumus_backend.domain.dtos.UserDto;

@Component
public class TestDtos {

    public UserDto dtoOne() {
        return new UserDto("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public UserDto dtoTwo() {
        return new UserDto("livia", "livia@gmail.com", "livia123", "11 123456789");
    }

    public UserDto dtoThree() {
        return new UserDto("alessandra", "alessandra@gmail.com", "alessandra123", "11 123456789");
    }

    public UserDto dtoFour() {
        return new UserDto("katia", "katia@gmail.com", "katia123", "11 123456789");
    }

    public UserDto dtoFive() {
        return new UserDto("rosangela", "rosangela@gmail.com", "rosangela123", "11 123456789");
    }

    public UserDto dtoSix() {
        return new UserDto("silvia", "silvia@gmail.com", "silvia123", "11 123456789");
    }

    public UserDto dtoSeven() {
        return new UserDto("luzinete", "luzinete@gmail.com", "luzinete123", "11 123456789");
    }

    public UserDto dtoEight() {
        return new UserDto("marialucia", "marialucia@gmail.com", "marialucia123", "11 123456789");
    }
}
