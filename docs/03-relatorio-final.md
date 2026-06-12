# SafeSolar - Relatorio Final de Imersao Profissional

## 1. Retrospectivas das sprints

### Sprint 1 - Setup e design

Funcionou: definicao clara do problema, stack coerente e separacao de responsabilidades. O modelo inicial cobriu
unidades, medicoes, rateios e alertas. Nao funcionou: o escopo IoT real era maior que o tempo disponivel. Aprendizado:
usar gateway HTTP e dados simulados no prototipo, preservando contrato para MQTT futuro.

### Sprint 2 - API principal

Funcionou: casos de uso separados por feature e Strategy eliminou condicionais no rateio. Nao funcionou: regras
de arredondamento surgiram tarde. Acao: criar invariante automatizada para garantir soma exata dos creditos.

### Sprint 3 - Integracao e persistencia

Funcionou: Docker Compose e Flyway tornaram o ambiente reproduzivel. Nao funcionou: conflitos de portas e diferencas
de ambiente causaram atraso. Acao: parametrizar porta externa e manter health checks.

### Sprint 4 - Testes e validacao

Funcionou: testes cobriram as regras de maior risco e a demonstracao passou com PostgreSQL real. Nao funcionou:
nao houve tempo para teste formal com usuarios externos nem autenticacao JWT. Esses itens permanecem no backlog.

## 2. Canvas e proposta de valor atualizados

### Proposta de valor

- Rateio de creditos reproduzivel, validado e auditavel.
- Deteccao precoce de queda de geracao e priorizacao operacional.
- Visao comparativa para reduzir desperdicio e disputas.
- Integracao aberta por API REST, sem dependencia de fabricante especifico.

### Segmentos

Administradoras de condominios, cooperativas solares, produtores rurais e integradores de energia fotovoltaica.

### Canais e relacionamento

Web app responsivo, onboarding assistido, demonstracoes para administradoras, suporte tecnico e base de conhecimento.

### Atividades, recursos e parceiros

Atividades: manter motor de rateio, integrar medidores, monitorar qualidade dos dados e garantir seguranca. Recursos:
plataforma Java/Angular, PostgreSQL, infraestrutura cloud e conhecimento regulatorio. Parceiros: integradores,
fabricantes de inversores, administradoras, cooperativas e provedores de nuvem.

### Receitas e custos

Receitas: assinatura por faixa de unidades, implantacao e relatorios premium. Custos: desenvolvimento, cloud,
observabilidade, suporte, seguranca, integracoes e adequacao regulatoria.

## 3. Product backlog simplificado

| Prioridade | Item | Criterio de aceite resumido |
|---|---|---|
| P0 | Autenticacao JWT e perfis | Administrador e participante acessam apenas dados autorizados |
| P0 | Auditoria imutavel | Toda confirmacao registra usuario, data e valores anteriores |
| P0 | Integracao MQTT | Leituras de broker homologado chegam idempotentemente |
| P1 | Front-end Angular responsivo | Fluxos de dashboard, rateio e alerta consomem a API |
| P1 | Notificacoes | Alertas criticos disparam e-mail/push configuravel |
| P1 | Importacao da distribuidora | Arquivo validado atualiza creditos sem duplicidade |
| P2 | Previsao de geracao/economia | Modelo exibe previsao e margem de erro |
| P2 | Exportacao PDF/CSV | Relatorios preservam filtros e periodo |
| P2 | Testes de carga e observabilidade | SLO e dashboards definidos para operacao |

## 4. Contribuicao para o ODS 7

O SafeSolar contribui diretamente para a meta 7.2 ao reduzir barreiras operacionais da geracao renovavel
compartilhada. O rateio transparente aumenta confianca e adesao; o monitoramento detecta perdas de producao; os
relatorios orientam consumo mais eficiente. O impacto deve ser medido por kWh renovavel aproveitado, reducao de
tempo no fechamento, quantidade de divergencias e disponibilidade dos sistemas de geracao.

O software nao gera energia sozinho e nao substitui conformidade regulatoria. Sua contribuicao ocorre ao tornar
a operacao coletiva mais confiavel, mensuravel e economicamente viavel.

## 5. Resultado da Entrega 2

Foi produzido backend Spring Boot funcional, PostgreSQL via Docker, migracoes, documentacao OpenAPI, tratamento de
excecoes e testes automatizados. As tres funcoes centrais planejadas foram validadas: monitoramento, rateio e
alertas. Relatorios ordenados ampliam a demonstracao de estruturas e algoritmos.
