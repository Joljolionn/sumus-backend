package com.sumus.sumus_backend.domain.dtos.response;

import java.util.List;

import com.sumus.sumus_backend.domain.entities.passenger.PassengerDocument;

public class PassengerListResponseDto {

    private List<PassengerDocument> passengers;

    public PassengerListResponseDto(List<PassengerDocument> passengers) {
        this.passengers = passengers;
    }

    public List<PassengerDocument> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDocument> passengers) {
        this.passengers = passengers;
    }

}
