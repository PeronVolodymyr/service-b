package com.example.serviceb.api.dto;

import java.time.Instant;

public record InfoResponse(String service, String message, Instant timestamp) {
}
