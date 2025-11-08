package com.sumus.sumus_backend.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Configuration
 * Classe de configuração central para o Spring Security.
 * * Atua como uma **Fábrica (Factory)** de Beans essenciais para a segurança,
 * como o PasswordEncoder e o SecurityFilterChain.
 * * A classe e seus métodos implementam o padrão **Factory Method** para
 * criar e configurar esses Beans.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * **Factory Method e Builder**: Configura e retorna o SecurityFilterChain.
     * * Utiliza o padrão **Builder** (através da API fluente do HttpSecurity) 
     * para montar passo a passo a cadeia de filtros de segurança da aplicação.
     * * No estado atual, desativa CSRF e permite acesso público a todas as requisições.
     * * @param http Objeto HttpSecurity injetado pelo Spring para construção.
     * @return O objeto SecurityFilterChain configurado.
     * @throws Exception Caso ocorra um erro durante a construção da cadeia de filtros.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disativa necessidade de tokens CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite todas as rotas sem necessidade de autenticação
                )
                .formLogin().disable(); // remove a página de login padrão

        return http.build();
    }
}
