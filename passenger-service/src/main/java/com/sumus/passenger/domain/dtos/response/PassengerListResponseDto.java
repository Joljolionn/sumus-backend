package com.sumus.passenger.domain.dtos.response;

import java.util.List;


public record PassengerListResponseDto(

    List<PassengerResponseDto> passengers

) {
}
