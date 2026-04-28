package com.sumus.routing_service.infra.clients.ors;

import java.util.List;

public record OrsRouteResponse(

    List<RouteRecord> routes) {

  public record RouteRecord(
      SummaryRecord summary,
      String geometry) {
  }

  public record SummaryRecord(
      Double distance,
      Double duration) {
  }
}
