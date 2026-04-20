package com.sumus.passenger.domain.dtos.response;

import java.util.List;

import com.sumus.passenger.domain.entities.PassengerDocument;

public record PassengerListResponseDto(

    List<PassengerDocument> passengers

) {
}
