# ADR 0001: Service B API and observability

## Status

Accepted

## Context

Service B is the HTTP callee in a two-Quarkus-service reference. This iteration ships application code only (no Kubernetes/ArgoCD). We need a stable contract for Service A, health endpoints suitable for future probes, and cross-request correlation without full distributed tracing yet.

## Decisions

### API: `GET /api/v1/info`

A versioned JSON endpoint returns `{ service, message, timestamp }`. That is enough for Service A to call over Kubernetes DNS and return a combined result later. The `service` field comes from `quarkus.application.name` so the name is not hardcoded in Java.

### JSON: `quarkus-rest-jackson`

Replaces scaffold `quarkus-rest`. Quarkus documents Jackson support as “instead of” the plain REST extension. `Instant` serializes as ISO-8601 with Quarkus defaults.

### Health: `quarkus-smallrye-health` only

Equivalent to Spring Boot Actuator health groups. Adding the extension auto-exposes `/q/health/live`, `/q/health/ready`, `/q/health/started`, and `/q/health`. No custom `@Liveness` / `@Readiness` checks: Service B is stateless with no external dependencies, so default readiness already means “able to serve traffic.” Hand-written `/health` JAX-RS resources are avoided so future Kubernetes probe generation stays aligned with MicroProfile Health paths.

### Correlation: `X-Correlation-Id` + MDC (not OpenTelemetry yet)

Acceptance criteria allow either full tracing or a propagated correlation ID. We implement:

- Quarkus REST `@ServerRequestFilter` / `@ServerResponseFilter`
- `org.jboss.logging.MDC` key `correlationId`
- Echo the id on the response; generate a UUID when the header is missing or unsafe

OpenTelemetry can replace or augment this later without changing the business API.

### Infra deferred

Cluster, registry, ArgoCD Applications, overlays, and image tag promotion stay out of this repository until a later iteration.

## Consequences

- Service A must call `GET /api/v1/info` and may send `X-Correlation-Id`.
- Deployments should probe `/q/health/live` and `/q/health/ready`.
- Engineers can run and verify the service with JDK 21 and `./mvnw` alone.
