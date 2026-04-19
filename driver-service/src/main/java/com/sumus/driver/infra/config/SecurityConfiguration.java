package com.sumus.driver.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sumus.driver.infra.security.jwt.JwtAuthenticationFilter;
import com.sumus.driver.infra.security.userdetails.DriverDetailsService;
import com.sumus.driver.infra.security.util.UserRole;

// Classe central para configurar segurança da aplicação
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Autowired
  private DriverDetailsService driverDetailsService;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable()) // Desativa necessidade de tokens CSRF
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin().disable() // remove a página de login padrão
        .authorizeHttpRequests(auth -> auth
            // .anyRequest().permitAll() // permite todas as rotas sem necessidade de
            // autenticação

            // Endpoints de login e signup são públicos
            .requestMatchers("/driver/login", "/driver/signup", "/driver/all", "/driver/teste-erro", "/error")
            .permitAll()

            // Endpoints de documentação são "públicos"
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html")
            .permitAll() // <-- Mantemos isso aqui

            // URLs específicas de usuário requerem autorização
            .requestMatchers("/driver/**").hasAuthority(UserRole.DRIVER.getAuthority())
            .anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }


  // --- Provedor de Autenticação para Motoristas ---
  @Bean
  public DaoAuthenticationProvider driverAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(driverDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }
}
