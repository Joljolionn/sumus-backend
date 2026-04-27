package com.sumus.routing_service.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import com.sumus.routing_service.domain.dtos.requests.RouteRequest;
import com.sumus.routing_service.domain.dtos.responses.RouteResponse;
import com.sumus.routing_service.infra.clients.ors.OrsRouteResponse;

@Controller
public class RoutesController {


  // Pegando essa URL de exemplo, desejamos enviar essas coordenadas e receber
  // um GeoJson bonito e pronto pra uso

  // curl
  // http://router.project-osrm.org/route/v1/driving/
  // -46.610913128835136,-23.69876589806748;
  // -46.6188566302654,-23.673245665039428?
  // steps=true

  @PostMapping(path = "/route")
  ResponseEntity<RouteResponse> returnRoute(@RequestBody
  RouteRequest req) {
    RestClient client = RestClient.builder()
        .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "identity")
        .build();

    String url = String.format(
        "http://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?overview=full&geometries=geojson",
        req.originX(), req.originY(), req.destX(), req.destY());

    OrsRouteResponse response = client.get()
        .uri(url)
        .retrieve()
        .body(OrsRouteResponse.class);

    if (response == null || response.routes().isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var route = response.routes().getFirst();

    RouteResponse routeResponse = new RouteResponse(
        route.distance(),
        route.duration(),
        route.geometry());

    return ResponseEntity.ok(routeResponse);
  }

}
