package com.sumus.driver.infra.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sumus.driver.domain.entities.DriverDocument;
import com.sumus.driver.repositories.DriverRepository;

import java.util.Optional;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initPassengerCollection(DriverRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {

            String testEmail = "Luzinete@sumus.com";

            System.out.println("---------------------------------------------");
            System.out.println(">>> INICIANDO CRIAÇÃO DE USUÁRIO DE TESTE <<<");


            Optional<DriverDocument> userOptional = repository.findByEmail(testEmail);


            if (userOptional.isPresent()) {
                System.out.println("Usuário de Teste (" + testEmail + ") já existe. Crie outro e-mail.");
                System.out.println("---------------------------------------------");
                return;
            }


            DriverDocument novoUsuario = new DriverDocument(
                    "Luzinete PP",
                    testEmail,
                    passwordEncoder.encode("senha321"), 
                    "11999999999", 
                    "11111111111"
            );

            DriverDocument savedUser = repository.save(novoUsuario);

            System.out.println("SUCESSO! Novo usuário de teste criado. Email: " + savedUser.getEmail());
            System.out.println("Senha de teste para login: senha321");
            System.out.println("---------------------------------------------");
        };
    }
}
