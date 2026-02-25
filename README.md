🏥 Clínica Médica Back-End
📌 Visão Geral

Este projeto é uma Aplicação Back-End para Gerenciamento de uma Clínica Médica, originalmente iniciado como parte de um trabalho acadêmico.

O sistema é construído em Java com Spring Boot, arquitetura modular (multi-módulos/microservices), e uma comunicação eficiente entre serviços via API Gateway, facilitando:

📊 Gestão de atendimentos médicos;

👨‍⚕️ Controle de agendamentos;

📁 Administração de informações administrativas e comuns;

🧠 Integração entre módulos;

🛠️ Fácil extensão para futuros módulos (ex: prontuário, convênios).

📂 Estrutura do Projeto

O repositório contém os principais módulos:

📦 clinica-medica-back-end1
 ┣ 📂 api-gateway
 ┣ 📂 clinica-medica-administrativo
 ┣ 📂 clinica-medica-agendamento
 ┣ 📂 clinica-medica-atendimento
 ┣ 📂 clinica-medica-comum
 ┣ 📂 collections
 ┣ 📂 containers
 ┣ 📂 eureka-server
 ┣ 📂 monitoring
 ┣ 📜 docker-compose.yml
 ┣ 📜 pom.xml
🧩 Módulos Principais
Módulo	Propósito
api-gateway	Ponto de entrada das APIs, roteamento e balanceamento.
eureka-server	Registro de serviços para descoberta e escalabilidade.
clinica-medica-comum	Classes e entidades compartilhadas entre módulos.
clinica-medica-administrativo	Lida com funcionalidades administrativas.
clinica-medica-agendamento	Gerencia marcação e consulta de horários.
clinica-medica-atendimento	Responsável pelos atendimentos clínicos propriamente ditos.
monitoring / containers / collections	Suporte, integração, testes e infraestrutura.
🚀 Tecnologias Usadas

✔️ Java 17
✔️ Spring Boot 3.1
✔️ Spring Data JPA
✔️ Spring Cloud (Eureka)
✔️ API Gateway (Spring Cloud Gateway)
✔️ ModelMapper
✔️ MySQL (driver)
✔️ OpenAPI / Swagger UI
✔️ Maven
✔️ Docker & Docker Compose

🛠️ Pré-Requisitos

Antes de instalar e rodar o projeto, certifique-se de ter:

☕ Java 17+

📦 Maven 3.8+

🐬 MySQL (ou qualquer outro banco compatível)

🐳 Docker & Docker-Compose (opcional para fácil deploy)

📥 Instalação & Execução
🧩 1. Clone o Repositório
git clone https://github.com/rogelio-fraga-dev/clinica-medica-back-end1.git
cd clinica-medica-back-end1
🐳 2. Executar com Docker

Caso tenha o Docker instalado, o docker-compose.yml já contém a configuração para os serviços principais:

docker compose up --build

Isso levantará:

✔️ Banco de dados
✔️ Eureka Server
✔️ API Gateway
✔️ Módulos principais

💻 3. Executar Localmente (Maven)

Execute individualmente cada módulo, por exemplo:

cd clinica-medica-comum
mvn clean install

cd ../clinica-medica-agendamento
mvn spring-boot:run

Repita para cada microserviço.

📌 Documentação da API

Após levantar os serviços, a documentação interativa via Swagger UI estará disponível (quando configurado) em:

http://localhost:<porta-do-serviço>/swagger-ui.html

Substitua <porta-do-serviço> por aquela definida no módulo, normalmente configurada nos arquivos application.yml ou application.properties.

🧪 Testes

Os módulos já possuem dependências para testes com Spring Boot Test — você pode adicionar testes unitários e de integração conforme necessário:

mvn test
📈 Roadmap Sugerido

💡 Funcionalidades que podem ser adicionadas futuramente:

🪪 Autenticação e autorização (JWT, OAuth2)

📑 Gerenciamento de prontuários eletrônicos

📊 Relatórios e dashboards

🧾 Integração com serviços de convênios

📱 Front-end (React/Vue/Angular) consumindo as APIs




👨‍💻 Autor

Desenvolvido por Rogélio Fraga — projeto feito na faculdade durante a matéria de PI: Projeto Integrador.
