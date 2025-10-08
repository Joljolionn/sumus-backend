# ESTÁGIO 1: BUILDER
# Usa uma imagem com JDK e Maven para compilar o código
FROM maven AS builder

# Define o diretório de trabalho do projeto
WORKDIR /app

# Copia o POM para baixar as dependências primeiro (aproveitando o cache do Docker)
COPY pom.xml .

# Baixa todas as dependências do projeto
RUN mvn dependency:go-offline

# Copia o código-fonte restante
COPY src/ ./src/

# Empacota o projeto em um JAR (o 'target/*.jar')
RUN mvn package -DskipTests

# --------------------------------------------------------------------------------------

# ESTÁGIO 2: EXECUÇÃO (Runtime)
# Usa uma imagem mínima que só tem o JRE (Java Runtime Environment)
FROM eclipse-temurin:21-jre-alpine AS runtime

# Define o diretório de trabalho do container
WORKDIR /app

# Copia o JAR criado no estágio 'builder' para este estágio
# Isso garante que a imagem final não inclua o Maven e o código-fonte
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
