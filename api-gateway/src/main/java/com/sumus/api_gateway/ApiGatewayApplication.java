package com.sumus.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@SpringBootApplication
public class ApiGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> gatewayRoutes() {
    return route("passenger-service")
        .route(path("/passenger/**"), http())
        .before(addRequestHeader("X-Gateway-Token", "Demo123"))
        .before(uri("http://passenger-service:8080"))
        .build()
        .and(route("driver-service")
            .route(path("/driver/**"), http())
            .before(addRequestHeader("X-Gateway-Token", "Demo123"))
            .before(uri("http://driver-service:8080"))
            .build())
        .and(route("routing-service")
            .route(path("/routing/**"), http())
            .before(addRequestHeader("X-Gateway-Token", "Demo123"))
            .before(uri("http://routing-service:8080"))
            .build());
  }
}
