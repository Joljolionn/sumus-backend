package com.sumus.sumus_backend.config;

import com.sumus.sumus_backend.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sumus.sumus_backend.domain.entities.UserDocument;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initUserCollection(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {

            String testEmail = "Luzinete@sumus.com";

            System.out.println("---------------------------------------------");
            System.out.println(">>> INICIANDO CRIAÇÃO DE USUÁRIO DE TESTE <<<");


            Optional<UserDocument> userOptional = repository.findByEmail(testEmail);


            if (userOptional.isPresent()) {
                System.out.println("Usuário de Teste (" + testEmail + ") já existe. Crie outro e-mail.");
                System.out.println("---------------------------------------------");
                return;
            }


            UserDocument novoUsuario = new UserDocument(
                    "Luzinete PP",
                    testEmail,
                    passwordEncoder.encode("senha321"), // Senha criptografada
                    "11999999999",
                    "Pass"
            );

            // 4. Salva o documento
            UserDocument savedUser = repository.save(novoUsuario);

            System.out.println("SUCESSO! Novo usuário de teste criado. Email: " + savedUser.getEmail());
            System.out.println("Senha de teste para login: senha123");
            System.out.println("---------------------------------------------");
        };
    }
}
