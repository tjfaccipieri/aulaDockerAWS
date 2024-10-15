# Estágio de build
FROM openjdk:21-jdk as build

# # Instalar dockerize para aguardar o banco de dados estar pronto
ADD https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz /tmp/
RUN tar -C /usr/local/bin -xzvf /tmp/dockerize-linux-amd64-v0.6.1.tar.gz

# Diretório de trabalho
WORKDIR /workspace/app

# Copiar arquivos do projeto
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Conceder permissões de execução ao mvnw
RUN chmod -R 777 ./mvnw

# Fazer o build do projeto
RUN ./mvnw install -DskipTests

# Extrair o conteúdo do JAR gerado para ser usado no estágio final
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Estágio final (execução)
FROM openjdk:21-jdk

# Copiar dockerize para o container final
COPY --from=build /usr/local/bin/dockerize /usr/local/bin/dockerize

# Argumento que aponta para o diretório da aplicação no estágio de build
ARG DEPENDENCY=/workspace/app/target/dependency

# Copiar os arquivos compilados para o container final
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Usar dockerize para esperar o banco de dados (serviço "database") na porta 5432
CMD ["dockerize", "-wait", "tcp://database:5432", "-timeout", "30s", "java", "-cp", "app:app/lib/*", "com.aulaaws.aulaws.AulawsApplication"]