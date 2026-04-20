package com.sumus.driver.domain.dtos.response;

import java.util.List;

import com.sumus.driver.domain.entities.DriverDocument;

public record DriverListResponseDTO(

    List<DriverDocument> drivers

) {
}
