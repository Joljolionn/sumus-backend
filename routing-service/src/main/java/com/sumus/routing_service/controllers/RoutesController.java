package com.sumus.routing_service.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import com.sumus.routing_service.domain.dtos.requests.RouteRequest;
import com.sumus.routing_service.domain.dtos.responses.RouteResponse;
import com.sumus.routing_service.infra.clients.ors.OrsRouteRequest;
import com.sumus.routing_service.infra.clients.ors.OrsRouteResponse;
import com.sumus.routing_service.infra.clients.ors.OrsRouteResponse.RouteRecord;

@Controller
public class RoutesController {

  @Value("${api.keys.ors}")
  private String orsToken;

  @PostMapping(path = "/route")
  ResponseEntity<RouteResponse> returnRoute(@RequestBody
  RouteRequest req) {
    RestClient client = RestClient.builder()
        .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "identity")
        .build();

    OrsRouteRequest orsRouteRequest = new OrsRouteRequest(List.of(
        new double[] {req.originX(), req.originY()},
        new double[] {req.destX(), req.destY()}));

    String url = "https://api.openrouteservice.org/v2/directions/driving-car";

    OrsRouteResponse response = client.post()
        .uri(url)
        .header("Authorization", orsToken.trim())
        .header("Content-Type", "application/json")
        .body(orsRouteRequest)
        .retrieve()
        .body(OrsRouteResponse.class);
    if (response == null || response.routes().isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    RouteRecord route = response.routes().getFirst();

    RouteResponse routeResponse = new RouteResponse(
        route.summary().distance(),
        route.summary().duration(),
        route.geometry());


    return ResponseEntity.ok(routeResponse);
  }
}
