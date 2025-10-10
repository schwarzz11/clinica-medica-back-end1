FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY clinica-medica-comum/pom.xml clinica-medica-comum/pom.xml
COPY clinica-medica-administrativo/pom.xml clinica-medica-administrativo/pom.xml
COPY clinica-medica-agendamento/pom.xml clinica-medica-agendamento/pom.xml
COPY clinica-medica-atendimento/pom.xml clinica-medica-atendimento/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY config-server/pom.xml config-server/pom.xml

RUN mvn -q -pl clinica-medica-administrativo -am dependency:go-offline

COPY . .
RUN mvn -q -pl clinica-medica-administrativo -am package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/clinica-medica-administrativo/target/clinica-medica-administrativo-*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
