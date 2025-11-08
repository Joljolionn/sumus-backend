# 🚀 Projeto Sumus Backend

O Sumus Backend é um projeto desenvolvido em **Spring Boot** utilizando o **MongoDB** como persistência de dados. A arquitetura segue o modelo de camadas, com uma forte adesão aos princípios **SOLID** e aos padrões de design **Gang of Four (GoF)**, o que garante um código limpo, desacoplado e de fácil manutenção.

---

## 🎯 Padrões GoF Aplicados na Arquitetura

O design deste projeto foi intencionalmente modelado em torno de padrões GoF, dividindo responsabilidades e promovendo a flexibilidade e reusabilidade do código.

### 🏭 Padrões Criacionais (Creational Patterns)

São responsáveis por fornecer mecanismos para a criação de objetos, otimizando o processo e o uso de recursos.

| Padrão | Onde se Manifesta | Descrição no Projeto |
| :--- | :--- | :--- |
| **Singleton** | Classes `@Component`, `@Service`, `@RestController` e `@Configuration` | Por *default*, o contêiner IoC do Spring Framework gerencia todos os **Spring Beans** (Controladores, Serviços, Configurações) como *Singletons*, garantindo que exista apenas uma instância desses componentes na aplicação. |
| **Factory Method** | Métodos `@Bean` nas classes `@Configuration` (`SecurityConfig`, `DatabaseInitializer`) e classes de *Test Data* (`PassengerDtoFactory`) | Utilizado para encapsular a lógica de criação e configuração de objetos complexos (como o `PasswordEncoder`, `SecurityFilterChain` e objetos de teste), delegando a responsabilidade de criação para métodos específicos. |
| **Builder (Construtor)** | Configuração de Segurança (`SecurityConfig`) | O método `@Bean` que constrói o `SecurityFilterChain` utiliza a API fluente do Spring Security (ex: `.csrf().disable().authorizeHttpRequests()`), que é uma aplicação direta do padrão **Builder** para montar o objeto de configuração de segurança passo a passo. |

---

### 🏗️ Padrões Estruturais (Structural Patterns)

Lidam com a composição de classes e objetos para formar estruturas maiores, mantendo-as flexíveis e eficientes.

| Padrão | Onde se Manifesta | Descrição no Projeto |
| :--- | :--- | :--- |
| **Facade (Fachada)** | Camada Controller (`AuthControllerImpl`, `PassengerControllerImpl`) | Os Controllers atuam como **Fachadas** para o subsistema de Serviço. Eles recebem as requisições HTTP e simplificam a interface para a lógica de negócio complexa (que envolve criptografia, persistência e validação), delegando a execução ao `PassengerService`. |


---

### 💡 Padrões Comportamentais (Behavioral Patterns)

Focam na comunicação e atribuição de responsabilidades entre objetos.

| Padrão | Onde se Manifesta | Descrição no Projeto |
| :--- | :--- | :--- |
| **Strategy (Estratégia)** | Relação entre a Interface de Serviço e sua Implementação (`PassengerService` e `PassengerServiceImpl`) | A arquitetura de Serviço/Implementação utiliza o **Strategy**. O Controller (o *Contexto*) depende da interface (`PassengerService`), permitindo que a implementação concreta (`PassengerServiceImpl` — a *Estratégia*) seja trocada (ou substituída por um *mock* em testes) sem alterar o código que a utiliza. |

