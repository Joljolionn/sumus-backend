package com.sumus.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import com.sumus.api_gateway.routes.AuthServiceRoutes;
import com.sumus.api_gateway.routes.DriverServicerRoutes;
import com.sumus.api_gateway.routes.PassengerServiceRoutes;
import com.sumus.api_gateway.routes.RoutingServiceRoutes;

@SpringBootApplication
public class ApiGatewayApplication {


  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> gatewayRoutes(
      AuthServiceRoutes authServiceRoutes,
      DriverServicerRoutes driverServicerRoutes,
      PassengerServiceRoutes passengerServiceRoutes,
      RoutingServiceRoutes routingServiceRoutes) {

    return authServiceRoutes.loginRoutes()
        .and(authServiceRoutes.authRoutes())
        .and(passengerServiceRoutes.routes())
        .and(driverServicerRoutes.routes())
        .and(routingServiceRoutes.routes());
  }
}
