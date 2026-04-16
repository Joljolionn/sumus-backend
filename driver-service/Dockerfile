# ESTÁGIO 1: BUILDER
# Usa uma imagem com JDK e Maven para compilar o código
FROM maven AS builder

# Define o diretório de trabalho do projeto
WORKDIR /app

# Copia o POM.xml pai para baixar as dependências primeiro (aproveitando o cache do Docker)
COPY pom.xml .

COPY driver-service/pom.xml ./driver-service/

# Instala o POM pai no repositório local do container
RUN mvn install -N

# Baixa todas as dependências do projeto
RUN mvn dependency:go-offline -pl driver-service -am

# Copia o código-fonte restante
COPY driver-service/src/ ./driver-service/src/

# Empacota o projeto em um JAR (o 'target/*.jar')
RUN mvn clean package -DskipTests -pl driver-service -am

# --------------------------------------------------------------------------------------

# ESTÁGIO 2: EXECUÇÃO (Runtime)
# Usa uma imagem mínima que só tem o JRE (Java Runtime Environment)
FROM eclipse-temurin:21-jre-alpine AS runtime

# Define o diretório de trabalho do container
WORKDIR /app

# Copia o JAR criado no estágio 'builder' para este estágio
# Isso garante que a imagem final não inclua o Maven e o código-fonte
COPY --from=builder /app/driver-service/target/*.jar app.jar

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
