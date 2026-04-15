# Backend Sumus - Guia de Execução

Este projeto utiliza o **Docker Compose** com o recurso de **Profiles** para gerenciar o ambiente de desenvolvimento de forma flexível. O Docker atua como o maestro, garantindo que o banco de dados e a aplicação subam em harmonia, sincronizados e limpos.

---

## Modos de Execução

Suportamos nativamente dois fluxos principais de trabalho:

### Apenas Banco de Dados (`db`)
Ideal para quando você quer rodar o backend (Spring/Java) diretamente na sua **IDE** (IntelliJ, VS Code, Eclipse), eliminando a necessidade de instalar o MongoDB localmente.

```bash
docker compose --profile db up
```

* **O que faz:** Sobe apenas o container do MongoDB.
* **Porta Externa:** `27018`
* **Persistência:** Os dados são mantidos no volume `mongo-data`.

### Ambiente Completo (`dev`)
Ideal para testar a aplicação exatamente como ela rodaria em um container, simulando o ambiente de produção/homologação.

```bash
docker compose --profile dev up
```

* **O que faz:** Sobe o MongoDB **e** constrói a imagem do backend a partir do `Dockerfile`.
* **Comunicação:** O backend e o banco conversam através da rede interna do Docker.

---

## Configuração do Sistema (Spring Profiles)

O comportamento da aplicação é gerenciado automaticamente via **Spring Profiles**, evitando alterações manuais em arquivos de configuração.

### 1. No Docker (Perfil `dev`)
Ao executar o profile `dev` no Docker, o container é iniciado com a variável `SPRING_PROFILES_ACTIVE=dev`.

* **Arquivo utilizado:** `application-dev.properties`.
* **Host do Banco:** Busca pelo nome do serviço: `mongodb:27017`.

### 2. Na IDE / Localmente (Perfil Padrão)
Se você rodar a aplicação pela IDE (após subir o banco com o profile `db`), o Spring usará o perfil padrão.

* **Arquivo utilizado:** `application.properties`.
* **Host do Banco:** Busca em `localhost:27018` (porta mapeada pelo Docker para acesso externo).

> **Atenção:** Evite alterar o `spring.data.mongodb.uri` diretamente no código. Se precisar usar um banco fora do Docker, passe a configuração via **variável de ambiente** na sua IDE para não "sujar" o arquivo compartilhado com o time. Recomenda-se reverter qualquer mudança temporária feita nesses arquivos antes de abrir um *Pull Request*.

---

## Resumo de Portas e Serviços

| Serviço | Porta Interna | Porta Externa (Host) | Perfil Docker |
| :--- | :--- | :--- | :--- |
| **MongoDB** | `27017` | `27018` | `db` ou `dev` |
| **Backend** | `8080` | `8080` | `dev` |

---

## Comandos de Utilidade

### Forçar Reconstrução (Reset do Build)
Use este comando se alterou o código e quer garantir que o Docker ignore o cache e crie uma imagem nova:
```bash
docker compose --profile dev up --build --force-recreate
```

### Encerrar o Ambiente
Para parar os serviços e liberar recursos:

* **Ambiente completo:** `docker compose --profile dev down -v`
* **Apenas o banco:** `docker compose --profile db down -v`

> **O que o `-v` faz?**
> A flag `-v` remove os **volumes**. No nosso caso, ela **apaga os dados** do MongoDB. Se desejar manter seus dados para a próxima sessão, execute o comando **sem** o `-v`.

---

## Estrutura do Maestro (Docker Compose)

* **Serviços:**
    * `mongodb`: Banco de dados NoSQL (versão 4.4).
    * `backend-dev`: Aplicação Spring Boot.
* **Rede (`sumus`):** Rede isolada que permite a comunicação entre os containers (Backend ↔ Banco ↔ Frontend) através dos nomes dos serviços.
* **Volumes:** `mongo-data` garante a persistência do estado do banco de dados.

---

> **Nota:** Sempre verifique se as portas `8080` ou `27018` já estão sendo usadas por outros processos antes de iniciar o ambiente.
