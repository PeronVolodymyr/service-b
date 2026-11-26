package com.example.serviceb.api;

import java.time.Instant;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.example.serviceb.api.dto.InfoResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/info")
public class InfoResource {

    private static final Logger LOG = Logger.getLogger(InfoResource.class);

    private final String serviceName;

    public InfoResource(@ConfigProperty(name = "quarkus.application.name") String serviceName) {
        this.serviceName = serviceName;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public InfoResponse info() {
        LOG.info("GET /api/v1/info request received");
        InfoResponse response = new InfoResponse(serviceName, "Hello from Service B", Instant.now());
        LOG.infof("GET /api/v1/info response: %s", response);
        return response;
    }
}
