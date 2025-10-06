# Orders Monolith (Spring Boot + PostgreSQL + Kafka)

Monólito para gestão de **Clientes, Produtos e Pedidos**. Persistência com **PostgreSQL**, migrações **Flyway**, publicação e consumo de eventos **PedidoCriado** via **Kafka**. API REST com **JSON** e **XML**.

## Stack
- Java 17, Spring Boot 3 (Web, Validation, Data JPA, Kafka)
- PostgreSQL 16, Flyway
- Docker Compose (Postgres, Kafka, App)

## Como rodar
```bash
./mvnw clean package -DskipTests
docker compose up --build -d
# App: http://localhost:8080
```

### Exemplos de Endpoints

#### Criar cliente (JSON)
```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Gabriel","email":"gabriel@acme.com"}'
```

#### Criar produto (XML)
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/xml" \
  -d '<product><name>Teclado</name><sku>TECL-001</sku><priceCents>12900</priceCents></product>'
```

#### Criar pedido (JSON)
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"customerEmail":"gabriel@acme.com","items":[{"productSku":"TECL-001","quantity":2}]}'
```

### Eventos
- **Tópico:** orders.pedido-criado.v1
- **Payload:**
```json
{
  "orderId": "UUID",
  "customerEmail": "gabriel@acme.com",
  "totalCents": 25800,
  "items": [{"productSku":"TECL-001","quantity":2,"priceCents":12900}]
}
```

## Arquitetura
- Camadas: domain (modelo e regras), application (serviços/DTOs), interfaces (REST), infrastructure (Kafka, config).
- CQS: consultas não alteram estado; criação de pedido dispara evento assíncrono.

## Testes
- Inclui teste unitário de regra de totalização (OrderTest).
- Recomenda-se expandir com Testcontainers para integração com Postgres/Kafka.

## Variáveis de ambiente
- SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD
- KAFKA_BOOTSTRAP (default localhost:9092)

## Boas práticas aplicadas
- **Nomes claros**, **funções coesas** e **guard clauses** para validação (fail-fast).
- **DTOs/Mapper** nas bordas; entidades JPA **não** expostas diretamente nos contratos externos.
- **Hexagonal leve**: domínio independente, adapters para HTTP/Kafka.
- **CQS** e **eventos** para integração assíncrona (aberto à extensão sem editar núcleo → **Open/Closed**).
- **Imutabilidade “onde dá”** (`record` nos DTOs); sem estado global mutável.
- **Observabilidade** mínima: logs no consumidor; pronto para métricas.
- **Performance**: `@EntityGraph` evita N+1 no GET de pedido; índices em FKs; BigInt em cents (sem `BigDecimal` para simplificar I/O).

## Checklist rápido de verificação
1. Nomes claros; métodos curtos; **guard clauses** no topo.
2. SOLID aplicado; dependências **injetadas**; sem acoplamento desnecessário.
3. Sem N+1 (uso de `@EntityGraph`); índices criados; projeções quando necessário.
4. Timeout/Retry/Backoff onde houver IO externo (adicionar em integrações futuras); logs estruturados.
5. Sem estado global mutável; **singletons só de infraestrutura** (beans Spring).
6. Build passa; app sobe com `docker compose up`; teste unitário verde.

## Guia para desenvolver
1. **Modelagem**: comece por `Order`, `OrderItem`, `Product`, `Customer`. Defina invariantes (quantidade > 0, preço >= 0).
2. **Persistência**: crie migrações Flyway e repositórios. **Nunca** use `ddl-auto=create` em prod.
3. **Serviço**: implemente `OrderService#create` calculando total a partir de itens. Lance exceções específicas para entradas inválidas.
4. **API**: exponha DTOs (JSON/XML). Ative `open-in-view=false` e busque com `@EntityGraph` no GET detalhado.
5. **Mensageria**: publique `PedidoCriadoEvent` após `commit` (aqui simplificado; em prod considere **outbox pattern**).
6. **Infra**: suba `Postgres` e `Kafka` via Compose. Parâmetros via env.
7. **Testes**: unitários de regras + integração (Testcontainers) para repositórios e publish/consume.
8. **Observabilidade**: padronize logs (`logfmt`/JSON), inclua `traceId`.
9. **Segurança**: adicionar Spring Security + tokens se for público; sanitize inputs; rate-limit.
10. **Hardening**: health checks, readiness, limites de memória (JVM flags no Dockerfile).

## Próximos passos (opcional)
- Adicionar **Outbox + Debezium** para entrega “exatamente uma vez” dos eventos.
- Paginação e filtros nos endpoints.
- Tabela de **payments** com transação saga (aí o evento vira gatilho real).
- Expor **OpenAPI/Swagger** e **metrics** (Micrometer + Prometheus).
