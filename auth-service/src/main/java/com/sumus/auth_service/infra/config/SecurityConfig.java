package com.sumus.auth_service.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.sumus.auth_service.infra.security.userdetails.driver.DriverDetailsService;
import com.sumus.auth_service.infra.security.userdetails.passenger.PassengerDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(form -> form.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/**")
            .permitAll())
        .build();
  }


  @Bean
  public DaoAuthenticationProvider passengerAuthenticationProvider(PassengerDetailsService passengerDetailsService,
        PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passengerDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    provider.setHideUserNotFoundExceptions(false);
    return provider;
  }


  @Bean
  public DaoAuthenticationProvider driverAuthenticationProvider(DriverDetailsService driverDetailsService,
        PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(driverDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    provider.setHideUserNotFoundExceptions(false);
    return provider;
  }
}
