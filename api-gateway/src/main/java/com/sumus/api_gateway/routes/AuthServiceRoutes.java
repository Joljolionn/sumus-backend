package com.sumus.api_gateway.routes;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class AuthServiceRoutes {

  public RouterFunction<ServerResponse> loginRoutes() {
    return route("passenger-login")
        .route(path("/passenger/login"), http())
        .before(uri("http://auth-service:8080/auth"))
        .build()
        .and(route("driver-login")
            .route(path("/driver/login"), http())
            .before(uri("http://auth-service:8080/auth"))
            .build());

  }

  public RouterFunction<ServerResponse> authRoutes() {
    return route("auth-service")
        .route(path("/auth/**"), http())
        .before(uri("http://auth-service:8080"))
        .build();
  }
}
