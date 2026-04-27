package com.sumus.routing_service.infra.clients.ors;

import java.util.List;

public record Leg(List<Object> steps, double weight, String summary, double duration, double distance) {
}
