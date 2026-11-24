package com.example.serviceb.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(InfoResource.class)
class InfoResourceTest {

    @Test
    void infoReturnsServicePayload() {
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body("service", equalTo("service-b"))
                .body("message", equalTo("Hello from Service B"))
                .body("timestamp", notNullValue());
    }
}
