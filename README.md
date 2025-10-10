# Clínica Médica - Back-end

## Visão Geral

Plataforma completa de microsserviços Spring Boot para gestão administrativa, agendamentos e atendimento clínico. A solução agora conta com configuração centralizada, observabilidade de ponta a ponta, pipeline CI e imagens Docker para facilitar execuções locais e prepará-la para ambientes de produção.

## Arquitetura

| Módulo | Descrição |
| ------ | --------- |
| `clinica-medica-comum` | Biblioteca compartilhada com entidades JPA, serviços reutilizáveis, DTOs, validações e infraestrutura cross-cutting (observabilidade, OpenAPI e filtros de correlação). |
| `clinica-medica-administrativo` | Microsserviço responsável por pacientes, funcionários, convênios, perfis e autenticação JWT. |
| `clinica-medica-agendamento` | Gerencia consultas médicas utilizando serviços e DTOs compartilhados. |
| `clinica-medica-atendimento` | Controla prontuários e integrações com consultas, pacientes e profissionais. |
| `api-gateway` | Gateway Spring Cloud com roteamento inteligente, rate limit e integração com Redis. |
| `eureka-server` | Service Discovery para os microsserviços. |
| `config-server` | Spring Cloud Config Server servindo propriedades para todos os módulos. |
| `monitoring` | Stack de observabilidade (Prometheus, Grafana e Jaeger). |

### Configuração Centralizada

* O `config-server` busca arquivos YAML em `config-repo/`, permitindo versionamento das propriedades.
* Cada serviço possui um `bootstrap.yml` apontando para o Config Server e mantendo perfis `local`, `docker`, `prod` e `test`.
* Novos ajustes devem ser realizados preferencialmente nos arquivos do `config-repo`.

### Observabilidade Avançada

* **Métricas**: counters customizados (`consultas_*`, `pacientes_*`) publicados via Micrometer/Prometheus.
* **Tracing distribuído**: Micrometer Tracing + OpenTelemetry OTLP exportando para Jaeger (coletor gRPC na porta `4317`).
* **Logging estruturado**: Logback com encoder JSON do Logstash e filtro de correlação (`X-Correlation-Id`).
* **Documentação**: OpenAPI centralizado em `OpenApiConfiguration`, com descrições e segurança JWT configuradas.

### Segurança e Padronização

* Endpoints expõem envelope `ApiResponse` uniforme e validam entradas com `jakarta.validation`.
* Controllers receberam documentação (`@Tag`) e logging semântico.
* Regras de autorização continuam centralizadas no módulo administrativo com JWT e suporte a cabeçalhos customizados.

## Como Executar Localmente

### Execução manual (IDE/Maven)
1. Suba os bancos de dados (se necessário) com `docker compose -f containers/docker-compose-db.yml up -d`.
2. Inicie o Config Server: `./mvnw spring-boot:run -pl config-server`.
3. Na sequência, execute Eureka, API Gateway e os três microsserviços (`spring-boot:run` em terminais separados).
4. Acesse o Eureka em `http://localhost:8761`, Swagger dos serviços em `/swagger-ui.html` via API Gateway e métricas em `/actuator/prometheus`.

### Execução completa com Docker Compose
```bash
docker compose up -d --build
```
Serviços disponíveis:
* Config Server: `http://localhost:8888`
* Eureka: `http://localhost:8761`
* API Gateway: `http://localhost:8088`
* Prometheus: `http://localhost:9090`
* Grafana: `http://localhost:3000`
* Jaeger: `http://localhost:16686`

Os contêineres MySQL dedicados ficam disponíveis nas portas **3307** (administrativo), **3308** (agendamento) e **3309** (atendimento).

Para ativar somente a stack de observabilidade utilize:

```bash
docker compose -f containers/docker-compose-grafana.yml up -d
```

## Qualidade e Testes

* `mvn verify` executa a suíte completa (unitários + relatórios JaCoCo agregados).
* Testes WebMvc cobrem fluxos de sucesso/validação nos controllers, enquanto testes de serviço utilizam `ObservationRegistry` real para validar métricas.
* Pipeline GitHub Actions (`.github/workflows/ci.yml`) roda automaticamente em pushes e pull requests, publicando o relatório de cobertura como artefato.

## Configurações Importantes

* `logback-spring.xml` em cada módulo garante logs JSON com MDC (`correlationId`, `traceId`, `spanId`).
* `CorrelationIdFilter` adiciona/propaga o cabeçalho `X-Correlation-Id`.
* `config-repo/*` concentra propriedades compartilhadas, com overrides específicos para perfis `docker`, `prod` e `test` (incluindo conexões H2 para testes automatizados).

## Roadmap Profissional

Status atualizado das fases do guia:

1. ✅ **Fase 1 – Padronização Estrutural**: pacotes renomeados (`config` → `configs`, `exception` → `exceptions`) e classes reorganizadas.
2. ✅ **Fase 2 – Arquivos de Configuração**: `application.properties` e `application-local.properties` recriados com padrão `local`.
3. ✅ **Fase 3 – Bancos de Dados**: Compose dedicado (`containers/docker-compose-db.yml`) com três instâncias MySQL isoladas.
4. ✅ **Fase 4 – Monitoramento Profissional**: Prometheus movido para `containers/monitoring/` e Compose (`docker-compose-grafana.yml`) para Prometheus/Grafana/Jaeger.
5. ✅ **Fase 5 – Segurança**: filtros e serviços consolidados no pacote `security`, mantendo autenticação JWT.
6. ⚠️ **Fase 6 – Testes e Qualidade**: suíte ampliada com testes de serviço, mas ainda faltam cenários de repositório e integração completa.
7. ✅ **Fase 7 – Config Server**: módulo dedicado ativo e microsserviços apontando para ele via `bootstrap.yml`.
8. ✅ **Fase 8 – Documentação**: Swagger/OpenAPI publicado em todos os domínios.
9. ✅ **Fase 9 – Deploy**: Dockerfiles individuais, Compose unificado e pipeline CI/GitHub Actions operando.
10. ⚠️ **Fase 10 – Finalização**: cobertura JaCoCo gerada, porém resta executar `mvn clean verify` em ambiente com acesso às dependências e criar a tag `v1.0.0`.

## Recursos Úteis

* **Coleções cURL/Postman**: diretório `collections`.
* **Scripts Docker**: `containers/docker-compose-db.yml` (bancos dedicados) e `containers/docker-compose-grafana.yml` (monitoramento).
* **OpenTelemetry Java Agent**: `opentelemetry-javaagent.jar` disponível para instrumentação adicional.

Sinta-se à vontade para abrir issues e evoluir ainda mais a plataforma!
