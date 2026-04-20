package com.sumus.passenger.infra.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sumus.passenger.domain.entities.PassengerDocument;
import com.sumus.passenger.repositories.PassengerRepository;

import java.util.Optional;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initPassengerCollection(PassengerRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {

            String testEmail = "Luzinete@sumus.com";

            System.out.println("---------------------------------------------");
            System.out.println(">>> INICIANDO CRIAÇÃO DE USUÁRIO DE TESTE <<<");


            Optional<PassengerDocument> userOptional = repository.findByEmail(testEmail);


            if (userOptional.isPresent()) {
                System.out.println("Usuário de Teste (" + testEmail + ") já existe. Crie outro e-mail.");
                System.out.println("---------------------------------------------");
                return;
            }


            PassengerDocument novoUsuario = new PassengerDocument(
                    "Luzinete PP",
                    testEmail,
                    passwordEncoder.encode("senha321"), 
                    "11999999999"
            );

            PassengerDocument savedUser = repository.save(novoUsuario);

            System.out.println("SUCESSO! Novo usuário de teste criado. Email: " + savedUser.getEmail());
            System.out.println("Senha de teste para login: senha321");
            System.out.println("---------------------------------------------");
        };
    }
}
