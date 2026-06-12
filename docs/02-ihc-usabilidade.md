# SafeSolar - IHC e Avaliacao de Usabilidade

## 1. Prototipo de alta fidelidade

O arquivo `docs/prototipo-ihc.html` e um prototipo visual responsivo executavel no navegador. Ele representa o
dashboard administrativo em desktop e mobile, com indicadores, grafico, tabela de unidades, fila de alertas e
acao principal de rateio. A interface segue a identidade azul, verde e solar definida na Entrega 1.

Telas previstas para o produto completo:

1. Login e recuperacao de acesso.
2. Dashboard de geracao, consumo, saldo e economia.
3. Unidades consumidoras e historico individual.
4. Assistente de rateio com simulacao, revisao e confirmacao.
5. Central de alertas priorizada.
6. Relatorios e exportacao.

## 2. Fluxo principal de interacao

1. O administrador entra no dashboard e verifica os indicadores e alertas.
2. Seleciona `Novo rateio` e informa periodo, creditos e estrategia.
3. O sistema calcula uma previa por unidade e destaca total e eventuais inconsistencias.
4. O administrador revisa e confirma explicitamente.
5. O rateio muda para `CONFIRMED` e permanece disponivel no historico auditavel.
6. Em caso de anomalia, o alerta aparece por severidade; o operador reconhece, investiga e resolve.

## 3. Decisoes de design

- Status nao depende apenas de cor: usa texto, icone e contraste.
- Numeros apresentam unidade (`kWh`, `%`, `R$`) e periodo de referencia.
- Acao destrutiva ou irreversivel exige confirmacao e resumo.
- Layout usa cards no desktop e uma coluna no mobile.
- Navegacao principal possui rotulos explicitos e foco visivel por teclado.
- Alertas aparecem no contexto e em central dedicada, reduzindo perda de informacao.

## 4. Avaliacao heuristica

A equipe realizou inspecao baseada nas dez heuristicas de Nielsen, usando o prototipo e os fluxos da API.

| Heuristica | Evidencia | Resultado/acao |
|---|---|---|
| Visibilidade do status | Indicadores, status do sistema e estados de alerta | Atendida |
| Correspondencia com o mundo real | kWh, unidade, consumo, geracao e credito | Atendida |
| Controle e liberdade | Simulacao antes da confirmacao | Atendida; incluir cancelamento no front-end |
| Consistencia | Mesmo vocabulario, cores e componentes | Atendida |
| Prevencao de erros | Validacao de periodo, cotas e soma do rateio | Atendida |
| Reconhecimento em vez de memoria | Menu e acoes visiveis | Atendida |
| Flexibilidade e eficiencia | Filtros e ordenacao nos relatorios | Parcial; adicionar favoritos |
| Design minimalista | Quatro KPIs e informacao operacional prioritaria | Atendida |
| Recuperacao de erros | API retorna mensagem e campo invalido | Atendida; front-end deve orientar correcao |
| Ajuda e documentacao | Swagger, manual e textos de apoio | Atendida |

## 5. Problemas identificados e melhorias

| Severidade | Problema | Correcao planejada |
|---|---|---|
| Alta | Confirmacao de rateio pode ser irreversivel | Modal com resumo, checkbox e trilha de auditoria |
| Media | Grafico pode ser dificil em telas muito pequenas | Alternar para tabela resumida acessivel |
| Media | Muitos alertas podem sobrecarregar o operador | Filtros, agrupamento por unidade e silenciamento temporario |
| Baixa | Usuarios novos podem nao conhecer estrategias | Tooltip e exemplo numerico no assistente |

## 6. Criterios de acessibilidade

- Contraste minimo WCAG AA para texto e controles.
- Navegacao integral por teclado e foco visivel.
- Rotulos associados aos campos e mensagens anunciadas por leitor de tela.
- Area minima de toque de 44 x 44 px no mobile.
- Tabelas com cabecalho semantico e alternativa textual para graficos.

## 7. Plano de teste com usuarios

Participantes: dois sindicos/administradores e um profissional de energia solar. Tarefas: localizar unidade com
menor saldo, simular rateio proporcional e resolver alerta critico. Metricas: conclusao sem ajuda, tempo, erros,
SUS e comentarios. Meta: 90% de conclusao, nenhum erro critico e SUS >= 75.
