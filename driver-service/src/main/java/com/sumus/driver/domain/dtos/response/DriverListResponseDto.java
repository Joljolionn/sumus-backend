package com.sumus.driver.domain.dtos.response;

import java.util.List;

public record DriverListResponseDto(

    List<DriverResponseDto> drivers

) {
}
