package com.sumus.routing_service.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .formLogin(form -> form.disable())
      .authorizeHttpRequests(auth -> auth
        .anyRequest().authenticated()
        // .anyRequest().permitAll() <- Liberar somente caso esteja fazendo testes
        )
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())).build();


  }
}
