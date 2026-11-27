#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVICE_NAME="${SERVICE_NAME:-service-b}"
TAG="${1:-${TAG:-}}"
REGISTRY="${REGISTRY:-k3d-quarkus-registry.localhost:5001}"
DOCKERFILE="${DOCKERFILE:-src/main/docker/Dockerfile.jvm}"

if [[ -z "${TAG}" ]]; then
  echo "Usage: TAG=<immutable-tag> $0 [tag]" >&2
  echo "   or: $0 <immutable-tag>" >&2
  exit 1
fi

IMAGE="${REGISTRY}/${SERVICE_NAME}:${TAG}"

echo "Building ${SERVICE_NAME} (JVM) in ${ROOT_DIR}"
cd "${ROOT_DIR}"
./mvnw -q package -DskipTests

echo "Building image ${IMAGE}"
docker build -f "${DOCKERFILE}" -t "${IMAGE}" .

echo "Pushing ${IMAGE}"
docker push "${IMAGE}"
echo "Pushed ${IMAGE}"
