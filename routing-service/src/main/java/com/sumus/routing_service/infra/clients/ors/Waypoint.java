package com.sumus.routing_service.infra.clients.ors;

import java.util.List;

public record Waypoint(String hint, List<Double> location, String name, Double distance) {
}
