package com.sumus.routing_service.infra.clients.ors;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Route(List<Leg> legs,

    @JsonProperty("weight_name")
    String weightName,

    Object geometry,
    Double weight,
    Double duration,
    Double distance) {
}
