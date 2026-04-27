package com.sumus.routing_service.infra.clients.ors;

import java.util.List;

public record OrsRouteResponse(String code, List<Route> routes, List<Waypoint> waypoints) {
}
