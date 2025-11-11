package com.sumus.sumus_backend.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sumus.sumus_backend.infra.security.util.UserRole;

// Classe central para configurar segurança da aplicação
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // Desativa necessidade de tokens CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // .formLogin().disable() // remove a página de login padrão
                .authorizeHttpRequests(auth -> auth
                        // .anyRequest().permitAll() // permite todas as rotas sem necessidade de
                        // autenticação

                        // Endpoints de login e signup são públicos
                        .requestMatchers("/passenger/login", "/passenger/signup", "/driver/login", "/driver/signup")
                        .permitAll()

                        // URLs específicas de usuário requerem autorização
                        .requestMatchers("/passenger/**").hasAuthority(UserRole.PASSENGER.getAuthority())
                        .requestMatchers("/driver/**").hasAuthority(UserRole.DRIVER.getAuthority())

                        .anyRequest().authenticated())
                .build();
    }
}
