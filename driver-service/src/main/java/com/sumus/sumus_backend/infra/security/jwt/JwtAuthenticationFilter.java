package com.sumus.sumus_backend.infra.security.jwt;

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

@Component // Marca a classe para ser gerenciada como um Bean pelo Spring (Component Scanning)
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Garante que o filtro será executado apenas uma vez por requisição HTTP

    @Autowired // Solicita a injeção de uma instância da classe de serviço de JWT (que lida com a lógica de token)
    private JwtService jwtService; // Objeto responsável por validar tokens, extrair claims (claims) e outras operações JWT

    @Override // Sobrescreve o método principal de execução do filtro
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) // Intercepta a requisição, a resposta e a cadeia de filtros
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); // Tenta extrair o cabeçalho 'Authorization' da requisição
        final String jwt; // Variável para armazenar o token JWT puro

        // INÍCIO DO PROCESSO DE VALIDAÇÃO DO HEADER
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Checa se o header está ausente OU se não possui o prefixo padrão "Bearer "
            filterChain.doFilter(request, response); // Se a condição acima for verdadeira (token ausente ou mal formatado), passa a requisição imediatamente para o próximo filtro
            return; // Encerra a execução deste filtro, não há token para processar
        }
        jwt = authHeader.substring(7); // Extrai o token JWT, removendo os 7 caracteres de "Bearer " (incluindo o espaço)

        // VALIDAÇÃO E EXTRAÇÃO DO USERNAME
        String username = jwtService.validateToken(jwt); // Chama o serviço para validar criptograficamente o token (assinatura, validade, etc.) e extrair o 'subject' (geralmente o username ou ID). Se inválido, deve retornar null.

        // VERIFICAÇÃO DE PRÉ-CONDIÇÕES PARA AUTENTICAÇÃO
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Confere se o username foi validado (token ok) E se o usuário AINDA NÃO está autenticado no contexto de segurança (evita reautenticar)

            // RECUPERAÇÃO DE PERMISSÕES DO TOKEN (STATELESS)
            String authorityString = jwtService.extractClaim(jwt, "role_type").asString(); // Extrai a 'role' (cargo/permissão) diretamente do payload do JWT (claim customizado 'role_type')

            List<GrantedAuthority> authorities = Collections.singletonList( // Cria uma lista imutável com uma única Authority
                    new SimpleGrantedAuthority(authorityString)); // Converte a string da role em um objeto 'GrantedAuthority' reconhecido pelo Spring Security

            // CRIAÇÃO DO PRINCIPAL (USERDETAILS)
            UserDetails userDetails = new User(username, "", authorities); // Cria um objeto UserDetails minimalista: username, senha vazia (irrelevante para JWT), e as autoridades extraídas do token.

            // CRIAÇÃO DO TOKEN DE AUTENTICAÇÃO
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( // Cria o Token de Autenticação, representando o usuário autenticado
                    userDetails, // O 'Principal' (a identidade)
                    null, // As 'Credenciais' (senha), que é 'null' pois o JWT já comprova a autenticidade
                    authorities // As 'Authorities' (permissões)
            );

            // ADIÇÃO DE DETALHES DA REQUISIÇÃO
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Adiciona detalhes como o endereço IP de origem e o ID da sessão (se houver) ao token para fins de auditoria e segurança.

            // CONFIGURAÇÃO DO CONTEXTO DE SEGURANÇA
            SecurityContextHolder.getContext().setAuthentication(authToken); // Define o token de autenticação no contexto de segurança da thread. Este é o ponto chave que autentica o usuário para o restante da requisição.
        }

        filterChain.doFilter(request, response); // Passa a requisição (agora potencialmente autenticada) para o próximo elemento na cadeia de filtros (ou para o Controller)
    }
}
