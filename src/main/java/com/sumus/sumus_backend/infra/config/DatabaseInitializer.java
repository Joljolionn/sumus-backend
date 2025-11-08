package com.sumus.sumus_backend.infra.config;

import com.sumus.sumus_backend.repositories.PassengerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sumus.sumus_backend.domain.entities.PassengerDocument;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * @Configuration
 * Esta classe de configuração atua como uma **Fábrica (Factory)** * para criar e fornecer **Beans de inicialização** (CommandLineRunner) 
 * ao contêiner IoC do Spring.
 * * Ela implementa o padrão **Factory Method** no seu método @Bean para
 * encapsular a lógica de criação e provisionamento de dados de teste no
 * banco de dados durante o startup da aplicação.
 * * A própria classe é um **Singleton** gerenciado pelo Spring.
 */
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
                    passwordEncoder.encode("senha321"), // Senha criptografada
                    "11999999999",
                    false,
                    null
            );

            // 4. Salva o documento
            PassengerDocument savedUser = repository.save(novoUsuario);

            System.out.println("SUCESSO! Novo usuário de teste criado. Email: " + savedUser.getEmail());
            System.out.println("Senha de teste para login: senha123");
            System.out.println("---------------------------------------------");
        };
    }
}
