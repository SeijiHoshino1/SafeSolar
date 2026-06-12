# SafeSolar - Documentacao Tecnica Consolidada

## 1. Escopo do prototipo

O backend valida as decisoes da Entrega 1 por meio de quatro capacidades testaveis: ingestao de medicoes,
rateio de creditos, deteccao/priorizacao de alertas e relatorios de eficiencia. A API REST e independente do
cliente e pode atender Angular, aplicativo mobile ou integracoes IoT via gateway HTTP/MQTT.

## 2. Arquitetura

O projeto segue arquitetura em camadas, organizada por funcionalidade:

- `controller`: contrato HTTP, validacao de entrada e codigos de resposta.
- `service`: casos de uso, transacoes e invariantes do dominio.
- `repository`: persistencia Spring Data JPA.
- entidades: mapeamento relacional e comportamento do dominio.
- `shared`: erros padronizados e tratamento global de excecoes.
- `db/migration`: schema e dados de demonstracao versionados pelo Flyway.

Fluxo: `Cliente -> Controller -> Service -> Repository -> PostgreSQL`. Eventos de leitura tambem seguem
`EnergyReadingService -> ReadingCreatedEvent -> GenerationDropObserver -> AlertRepository`.

## 3. Modelo de dados

| Entidade | Finalidade | Relacionamentos |
|---|---|---|
| `ConsumerUnit` | Unidade participante e sua cota fixa | 1:N com medicoes, alocacoes e alertas |
| `EnergyReading` | Geracao/consumo em kWh e instante de coleta | N:1 com unidade |
| `CreditDistribution` | Cabecalho auditavel de um rateio | 1:N com alocacoes |
| `CreditAllocation` | Credito e percentual aplicado por unidade | N:1 com rateio e unidade |
| `Alert` | Evento tecnico, severidade e ciclo de atendimento | N:1 com unidade |

As chaves estrangeiras impedem referencias inexistentes. `NUMERIC` evita erros monetarios/energeticos de ponto
flutuante. Os indices `idx_reading_unit_time` e `idx_alert_status_severity` aceleram historicos e alertas abertos.

## 4. Endpoints REST

| Metodo | Endpoint | Uso |
|---|---|---|
| `POST` | `/api/v1/units` | Cadastrar unidade |
| `GET` | `/api/v1/units` | Listar unidades |
| `POST` | `/api/v1/readings` | Registrar leitura IoT/manual |
| `GET` | `/api/v1/readings?unitId=&start=&end=` | Consultar historico |
| `POST` | `/api/v1/distributions/calculate` | Calcular rateio |
| `PATCH` | `/api/v1/distributions/{id}/confirm` | Confirmar rateio |
| `GET` | `/api/v1/distributions` | Consultar auditoria de rateios |
| `POST` | `/api/v1/alerts` | Criar alerta |
| `GET` | `/api/v1/alerts` | Listar pendencias priorizadas |
| `PATCH` | `/api/v1/alerts/{id}/status?status=` | Reconhecer/resolver alerta |
| `GET` | `/api/v1/reports/efficiency` | Comparar eficiencia das unidades |

O contrato interativo completo esta em `/swagger-ui.html`. Respostas de erro contem horario, status HTTP,
mensagem, rota e erros de campo. Regras invalidas usam `422`; entrada malformada usa `400`; recurso ausente usa `404`.

## 5. Recursos avancados de Java e Spring

- Records para DTOs imutaveis.
- Streams, lambdas, generics e `Comparator` para transformacao e ordenacao.
- Strategy Pattern para algoritmos intercambiaveis de rateio.
- Observer Pattern com eventos Spring para deteccao desacoplada de anomalias.
- Injecao de dependencias por construtor e mapa imutavel de estrategias.
- Transacoes declarativas e consultas JPQL agregadas.
- Bean Validation e `@RestControllerAdvice` para erros consistentes.
- Entity Graph para carregar rateio, alocacoes e unidades sem problema N+1 na serializacao.

## 6. Estruturas de dados e complexidade

### Fila de prioridade

`AlertService` carrega alertas pendentes em `PriorityQueue<Alert>`. A severidade e a chave primaria; o instante
desempata. Insercao e remocao custam `O(log n)`, consulta ao topo custa `O(1)` e a listagem completa `O(n log n)`.

### Hash map

O consumo e associado ao ID da unidade em `Map<Long, BigDecimal>`. Busca e insercao possuem custo medio `O(1)`;
o calculo das participacoes percorre as `n` unidades em `O(n)`. O mapa de estrategias tambem seleciona o algoritmo
em tempo medio `O(1)`.

### Lista

As alocacoes usam `ArrayList`, adequada para percurso sequencial e insercao no final amortizada `O(1)`. O calculo
completo do rateio e `O(n)` e usa memoria adicional `O(n)`.

### Quicksort

O relatorio usa uma implementacao generica de Quicksort. Tempo medio `O(n log n)`, pior caso `O(n²)` e pilha media
`O(log n)`. O comparador permite ordenar por geracao, consumo ou saldo sem duplicar o algoritmo.

### Pesquisa e banco

Consultas por chave primaria usam o indice B-tree do PostgreSQL, com custo esperado `O(log n)`. Historicos usam
indice composto `(consumer_unit_id, recorded_at)`, permitindo localizar a faixa em `O(log n + k)`, sendo `k` a
quantidade de registros retornados. As somas sao executadas no banco para evitar transportar dados desnecessarios.

## 7. Regras e integridade

- As cotas fixas ativas devem somar 100%.
- Rateio proporcional exige consumo positivo no periodo.
- A ultima alocacao recebe o residuo decimal; logo, soma das alocacoes = total disponivel.
- Um rateio confirmado nao pode ser confirmado novamente.
- Leituras do mesmo tipo nao podem retroceder no tempo.
- Queda de geracao igual ou superior a 40% cria alerta `HIGH` automaticamente.
- Alertas resolvidos nao podem voltar para estado aberto pelo endpoint atual.

## 8. Testes implementados

| Teste | Validacao |
|---|---|
| `CreditDistributionStrategyTest` | Cota fixa, proporcionalidade, arredondamento e periodo sem consumo |
| `GenerationDropObserverTest` | Criacao de alerta para queda superior a 40% |
| `AlertServiceTest` | Prioridade por severidade e data |
| `QuickSortTest` | Ordenacao generica correta |
| `ConsumerUnitControllerTest` | Contrato JSON e validacao HTTP do endpoint de unidades |

O build `docker compose build api` executa `mvn clean package`, incluindo todos os testes. H2 em modo PostgreSQL
isola testes que necessitem de contexto; Mockito isola colaboradores nos testes unitarios.

## 9. Implantacao e operacao

Consulte o `README.md` para instalacao. Em producao, senhas devem vir de secrets, HTTPS deve terminar em proxy ou
load balancer e o volume PostgreSQL deve possuir backup. O endpoint `/actuator/health` verifica aplicacao e banco.
