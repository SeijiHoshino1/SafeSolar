# SafeSolar

Backend funcional da plataforma de gestao de microgeracao solar compartilhada. O prototipo monitora medicoes,
automatiza o rateio auditavel de creditos, prioriza alertas tecnicos e gera relatorios de eficiencia.

## Funcionalidades implementadas

1. Cadastro de unidades consumidoras e registro de medicoes de geracao/consumo.
2. Rateio de creditos por cota fixa ou consumo proporcional, preservando a soma exata do total.
3. Alertas manuais e automaticos por queda brusca de geracao, ordenados por prioridade.
4. Relatorio comparativo ordenado por geracao, consumo ou saldo.
5. API OpenAPI/Swagger, validacao, tratamento global de excecoes e health check.

## Tecnologias

- Java 21 e Spring Boot 3.5
- Spring Web, Validation, Data JPA e Actuator
- PostgreSQL 17, Flyway e Docker Compose
- JUnit 5, AssertJ e Mockito
- springdoc-openapi/Swagger UI

## Execucao rapida

Pre-requisito: Docker Desktop em execucao.

```powershell
docker compose up --build -d
```

Servicos:

- API: <http://localhost:8081>
- Swagger UI: <http://localhost:8081/swagger-ui.html>
- Health check: <http://localhost:8081/actuator/health>
- PostgreSQL: `localhost:5432` (`safesolar/safesolar`)

A porta externa pode ser alterada com `API_PORT=8090`. Para encerrar:

```powershell
docker compose down
```

Use `docker compose down -v` somente quando quiser apagar os dados locais.

## Testes

O build da imagem executa a suite automaticamente:

```powershell
docker compose build api
```

Com Maven 3.9+ instalado, tambem e possivel executar:

```powershell
mvn test
mvn spring-boot:run
```

Nesse segundo modo, inicie apenas o banco com `docker compose up -d postgres`; a API usa a porta `8080` por padrao.

## Documentacao da entrega

- [Documentacao tecnica](docs/01-documentacao-tecnica.md)
- [IHC e avaliacao de usabilidade](docs/02-ihc-usabilidade.md)
- [Relatorio final](docs/03-relatorio-final.md)
- [Roteiro do video](docs/04-roteiro-video.md)
- [Prototipo visual responsivo](docs/prototipo-ihc.html)
- [Collection e guia do Postman](postman/README.md)
