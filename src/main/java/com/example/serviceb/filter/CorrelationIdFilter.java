package com.example.serviceb.filter;

import java.util.UUID;
import java.util.regex.Pattern;

import org.jboss.logging.MDC;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;

public class CorrelationIdFilter {

    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    private static final String REQUEST_PROPERTY = CorrelationIdFilter.class.getName() + ".id";
    private static final Pattern SAFE_ID = Pattern.compile("[A-Za-z0-9._:-]{1,128}");

    @ServerRequestFilter
    public void onRequest(ContainerRequestContext requestContext) {
        String incoming = requestContext.getHeaderString(HEADER);
        String correlationId = isSafe(incoming) ? incoming : UUID.randomUUID().toString();

        requestContext.setProperty(REQUEST_PROPERTY, correlationId);
        MDC.put(MDC_KEY, correlationId);
    }

    @ServerResponseFilter
    public void onResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        try {
            Object correlationId = requestContext.getProperty(REQUEST_PROPERTY);
            if (correlationId != null) {
                responseContext.getHeaders().putSingle(HEADER, correlationId.toString());
            }
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    private static boolean isSafe(String value) {
        return value != null && SAFE_ID.matcher(value).matches();
    }
}
