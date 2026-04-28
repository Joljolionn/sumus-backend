package com.sumus.routing_service.infra.clients.ors;

import java.util.List;

public record OrsRouteRequest(
    List<double[]> coordinates,
    String units,
    boolean instructions) {
  public OrsRouteRequest(List<double[]> coordinates) {
    this(coordinates, "m", false);
  }
}
