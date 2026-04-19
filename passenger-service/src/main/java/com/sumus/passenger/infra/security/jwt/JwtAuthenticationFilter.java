package com.sumus.passenger.infra.security.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class JwtAuthenticationFilter extends OncePerRequestFilter { 

    @Autowired 
    private JwtService jwtService; 

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); 
        final String jwt; 

        // INÍCIO DO PROCESSO DE VALIDAÇÃO DO HEADER
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { 
            filterChain.doFilter(request, response); 
            return; 
        }
        jwt = authHeader.substring(7); 

        // VALIDAÇÃO E EXTRAÇÃO DO USERNAME
        String username = jwtService.validateToken(jwt); 

        // VERIFICAÇÃO DE PRÉ-CONDIÇÕES PARA AUTENTICAÇÃO
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 

            // RECUPERAÇÃO DE PERMISSÕES DO TOKEN (STATELESS)
            String authorityString = jwtService.extractClaim(jwt, "role_type").asString(); 

            List<GrantedAuthority> authorities = Collections.singletonList( 
                    new SimpleGrantedAuthority(authorityString)); 

            // CRIAÇÃO DO PRINCIPAL (USERDETAILS)
            UserDetails userDetails = new User(username, "", authorities); 

            // CRIAÇÃO DO TOKEN DE AUTENTICAÇÃO
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( 
                    userDetails, 
                    null, 
                    authorities 
            );

            // ADIÇÃO DE DETALHES DA REQUISIÇÃO
            // Adiciona detalhes como o endereço IP de origem e o ID da sessão (se houver) ao token para fins de auditoria e segurança.
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 

            // CONFIGURAÇÃO DO CONTEXTO DE SEGURANÇA
            SecurityContextHolder.getContext().setAuthentication(authToken); 
        }

        filterChain.doFilter(request, response); 
    }
}
