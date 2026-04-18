# Backend Sumus - Guia de Execução

Este projeto utiliza o **Docker Compose** com o recurso de **Profiles** para gerenciar um ambiente de microsserviços de forma flexível. O Docker atua como o maestro, garantindo que os bancos de dados e as aplicações subam em harmonia.

---

## Modos de Execução

Suportamos nativamente dois fluxos principais de trabalho:

### Apenas Bancos de Dados (`db`)
Ideal para quando você quer rodar os serviços (Spring/Java) diretamente na sua **IDE** (IntelliJ, VS Code, Eclipse), eliminando a necessidade de instalar o MongoDB localmente.

```bash
docker compose --profile db up
```

* **O que faz:** Sobe o container do MongoDB principal e o MongoDB de testes.
* **Portas Externas:** `27018` (Principal) e `27019` (Testes).
* **Persistência:** Dados do banco principal mantidos no volume `mongo-data`.

#### Desenvolvendo um serviço isolado (IDE)
Se você for mexer em apenas um dos serviços (ex: apenas no `driver-service`), siga estes passos:
1. Execute o comando acima (`--profile db`).
2. Abra o projeto do serviço desejado na sua IDE.
3. Certifique-se de que o perfil do Spring está como **default** (o Spring lerá o `application.properties` que aponta para `localhost:27018`).
4. Execute a classe `Application` principal.

> **Atenção:** Se precisar rodar o segundo serviço simultaneamente na IDE, você deverá alterar a porta da aplicação (ex: `-Dserver.port=8081`) para evitar conflito na porta `8080` da sua máquina local.

### Ambiente Completo (`dev`)
Ideal para testar a integração entre os microsserviços exatamente como rodariam em um container.

```bash
docker compose --profile dev up
```

* **O que faz:** Sobe ambos os bancos de dados **e** constrói as imagens do `driver-service` e `passenger-service`.

---

## Configuração do Sistema (Spring Profiles)

O comportamento da aplicação é gerenciado automaticamente via **Spring Profiles** em três cenários:

| Cenário | Perfil Ativo | Arquivo | URI do MongoDB |
| :--- | :--- | :--- | :--- |
| **Docker (dev)** | `dev` | `application-dev.properties` | `mongodb://mongodb:27017` |
| **Local (IDE)** | `default` | `application.properties` | `mongodb://localhost:27018` |
| **Testes** | `test` | `application.properties` (test) | `mongodb://localhost:27019` |

### Segurança e JWT
Ambos os serviços utilizam a chave `JWT_SECRET` para autenticação. 
* Em ambiente de desenvolvimento, o valor padrão é `maria-do-carmo`.
* **Configuração via Variável:** Para mudar a chave sem alterar o código, defina: `JWT_SECRET=sua_chave_aqui`.

---

## Resumo de Portas e Serviços

| Serviço | Porta Interna | Porta Externa (Host) | Perfil Docker |
| :--- | :--- | :--- | :--- |
| **MongoDB (Principal)** | `27017` | **`27018`** | `db` ou `dev` |
| **MongoDB (Testes)** | `27017` | **`27019`** | `db` ou `dev` |
| **Driver Service** | `8080` | **`8080`** | `dev` |
| **Passenger Service** | `8080` | **`8081`** | `dev` |

---

## Comandos de Utilidade

### Forçar Reconstrução
```bash
docker compose --profile dev up --build --force-recreate
```

### Encerrar o Ambiente
* **Ambiente completo:** `docker compose --profile dev down -v`
* **Apenas os bancos:** `docker compose --profile db down -v`

---

## Notas Técnicas e Arquitetura

* **Persistência:** O volume `mongo-data` garante a durabilidade dos dados entre reinicializações do container `mongodb`.
* **Compatibilidade:** Utilizamos MongoDB v4.4 para garantir suporte a CPUs que não possuem instruções AVX.
* **Ambiente de Testes:** O arquivo `application.properties` de teste aponta para a porta `27019`. 
> **Importante:** Para que os testes de integração funcionem localmente, o
> perfil `db` do Docker precisa estar ativo para prover a instância
> `mongo_test`. Caso contrário, não se esqueça de utilizar a opção
> **`-DskipTests`** para pular a etapa de testes. Ex: **`mvn spring-boot:run -DskipTests`**
* **Escalabilidade de Arquivos:** As configurações de `multipart` (limite de 5MB) estão padronizadas entre os serviços para garantir consistência no upload de documentos e fotos.
