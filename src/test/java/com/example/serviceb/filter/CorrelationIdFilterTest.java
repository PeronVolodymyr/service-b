package com.example.serviceb.filter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class CorrelationIdFilterTest {

    @Test
    void echoesProvidedCorrelationId() {
        given()
                .header(CorrelationIdFilter.HEADER, "demo-correlation-1")
                .when().get("/api/v1/info")
                .then()
                .statusCode(200)
                .header(CorrelationIdFilter.HEADER, equalTo("demo-correlation-1"));
    }

    @Test
    void generatesCorrelationIdWhenMissing() {
        given()
                .when().get("/api/v1/info")
                .then()
                .statusCode(200)
                .header(CorrelationIdFilter.HEADER, not(emptyOrNullString()))
                .header(CorrelationIdFilter.HEADER,
                        matchesPattern("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
    }
}
