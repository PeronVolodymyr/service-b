# service-b

Quarkus callee service for the two-service HTTP reference implementation. Service A will call this service over HTTP; this repository contains **application code only** (Kubernetes, ArgoCD, and bootstrap come in later iterations).

## Prerequisites

- JDK 21
- Maven Wrapper is included (`./mvnw`) — no system Maven required

## Run locally

```bash
./mvnw quarkus:dev
```

Dev UI: http://localhost:8080/q/dev/

## API contract

### `GET /api/v1/info`

Returns a JSON payload that a caller can combine with its own response.

```bash
curl http://localhost:8080/api/v1/info
```

Example:

```json
{
  "service": "service-b",
  "message": "Hello from Service B",
  "timestamp": "2026-11-24T21:00:00Z"
}
```

### Correlation ID

Send `X-Correlation-Id` to correlate logs across services. If omitted or invalid, Service B generates a UUID and returns it on the response.

```bash
curl -i -H 'X-Correlation-Id: demo-1' http://localhost:8080/api/v1/info
```

Logs include `correlationId=...` via MDC.

## Health (SmallRye Health)

Same role as Spring Boot Actuator health groups. Endpoints are exposed automatically by `quarkus-smallrye-health` — there are no hand-written `/health` resources.

| Endpoint | Role |
|----------|------|
| `/q/health/live` | Liveness |
| `/q/health/ready` | Readiness |
| `/q/health` | Aggregated |

```bash
curl http://localhost:8080/q/health/ready
```

## Tests

```bash
./mvnw test
```

## Package

```bash
./mvnw package
```

JVM runner: `java -jar target/quarkus-app/quarkus-run.jar`

Stock containerfiles live under `src/main/docker/` for a later image-build iteration.

## Design notes

See [docs/adr/0001-service-b-api-and-observability.md](docs/adr/0001-service-b-api-and-observability.md).

## Out of scope (later)

- Local Kubernetes / ArgoCD / GitOps manifests
- Immutable image tagging and promotion scripts
- Service A REST client, resilience, and WireMock tests
